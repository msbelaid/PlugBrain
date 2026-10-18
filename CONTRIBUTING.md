# Contributing to PlugBrain!
PlugBrain is a free and open-source project. Since the app does not collect user data, your feedback and contributions are especially valuable.  

There are many ways you can help:  

---

## Feedback
- Share your experience using the app.  
- Report problems you encounter.  
- Suggest features or improvements.  
Since no analytics are collected, your feedback is the best way for us to improve PlugBrain.  

---

## Translation
Help make PlugBrain available to more people around the world:  
1. Translate app strings in [`strings.xml`](app/src/main/res/values/strings.xml).  
2. Translate the stores metadata (Fastlane files):  
   - App description: `fastlane/metadata/android/<lang>/full_description.txt`  
   - Short description: `fastlane/metadata/android/<lang>/short_description.txt`  
   - Screenshots: `fastlane/metadata/android/<lang>/images/phoneScreenshots/`  

---

## Creating Issues
You can open an issue to:  
- Report a bug
- Suggest a new challenge  
- Request a feature or improvement  
- Ask a question about the project  

---

## Working on an Issue
- Check the [Issues](../../issues) tab for open tickets.  
- Comment on the ticket to let others know you're working on it.  

---

## Creating a new Challenge type
PlugBrain supports math challenges where the user must provide a numeric answer.  
You can scaffold a new challenge automatically using the Gradle generator:  

```bash
./gradlew generateChallenge -PchallengeName=MyNewChallenge
```
After running this command, your job is simply to update the generated Challenge model:
### Challenge model (MyNewChallenge.kt)
- Add any required fields
- Set the difficultyLevel property
- Implement the checkAnswer() function with your validation logic
- Override string() to define how the challenge is displayed

The challenge screen shows `string()` inside the design system's `PlugCard`, so no UI code is needed.

Note: At the moment, only numeric-answer challenges are supported.

### Example
PlugBrain supports math-like challenges where the user must provide a numeric answer.
In this challenge, the user is asked to count the number of stars displayed on screen.

You can scaffold a new challenge automatically using the Gradle generator:
```bash
./gradlew generateChallenge -PchallengeName=CountingChallenge
```

#### Update the Model
The generator will create a new file under `app.plugbrain.android.challenges.counting.CountingChallenge`.
Update it with the following fields:
```kotlin
class CountingChallenge : NumericalChallenge {
  val totalCircles: Int = Random.nextInt(3, 10)
  override fun checkAnswer(response: Int): Boolean {
    return response == totalCircles
  }
  override val difficultyLevel = 0
  override fun string() = "*".repeat(totalCircles)
}
```

