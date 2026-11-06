# Store listing text shared by F-Droid and Google Play.
#
# fastlane/metadata/android stays in the layout F-Droid and IzzyOnDroid read. Play wants other codes for
# a few locales, so the listing lane copies the text into a temp folder named the Play way.
#
# Run `ruby fastlane/store_listing.rb` to check every locale against Play's limits (CI does on PRs).
module StoreListing
  METADATA_DIR = File.expand_path("metadata/android", __dir__)
  # Google Play only: framed phoneScreenshots/<n>.png and a 1024x500 featureGraphic.png per locale folder.
  # F-Droid keeps the plain screenshots in METADATA_DIR.
  PLAY_IMAGES_DIR = File.expand_path("play-images", __dir__)

  # F-Droid folder => Play language code, where they differ.
  PLAY_CODES = { "hi" => "hi-IN", "ja" => "ja-JP", "nb-NO" => "no-NO" }.freeze

  # Play's character limits.
  LIMITS = { "title.txt" => 30, "short_description.txt" => 80, "full_description.txt" => 4000 }.freeze
  CHANGELOG_LIMIT = 500

  module_function

  def locales
    Dir.children(METADATA_DIR).select { |name| File.directory?(File.join(METADATA_DIR, name)) }.sort
  end

  def play_code(locale)
    PLAY_CODES.fetch(locale, locale)
  end

  def text(path)
    File.read(path, encoding: "UTF-8").strip
  end

  # Every problem Play would reject, as "locale/file: reason".
  def problems
    locales.flat_map do |locale|
      dir = File.join(METADATA_DIR, locale)
      texts = LIMITS.flat_map do |file, limit|
        path = File.join(dir, file)
        next ["#{locale}/#{file}: missing"] unless File.exist?(path)

        length = text(path).length
        next ["#{locale}/#{file}: empty"] if length.zero?

        length > limit ? ["#{locale}/#{file}: #{length} characters, Play allows #{limit}"] : []
      end
      changelogs = Dir.glob(File.join(dir, "changelogs", "*.txt")).sort.filter_map do |path|
        length = text(path).length
        "#{locale}/changelogs/#{File.basename(path)}: #{length} characters, Play allows #{CHANGELOG_LIMIT}" if length > CHANGELOG_LIMIT
      end
      texts + changelogs
    end
  end

  # A folder Play can read: <dir>/<Play code>/{title,short_description,full_description}.txt, plus
  # images/featureGraphic.png and images/phoneScreenshots/ from PLAY_IMAGES_DIR where a locale has them.
  def play_listing_dir(root)
    locales.each do |locale|
      target = File.join(root, play_code(locale))
      FileUtils.mkdir_p(target)
      LIMITS.each_key { |file| FileUtils.cp(File.join(METADATA_DIR, locale, file), target) }

      images = File.join(PLAY_IMAGES_DIR, locale)
      next unless File.directory?(images)

      FileUtils.mkdir_p(File.join(target, "images"))
      FileUtils.cp_r(Dir.glob(File.join(images, "*")), File.join(target, "images"))
    end
    root
  end
end

if $PROGRAM_NAME == __FILE__
  found = StoreListing.problems
  if found.empty?
    puts "Store listing OK: #{StoreListing.locales.join(', ')}"
  else
    puts found.map { |problem| "::error::#{problem}" }
    exit 1
  end
end
