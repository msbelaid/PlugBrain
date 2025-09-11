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
After running this command, your job is simply to update the generated Challenge model and its Compose screen:
### Challenge model (MyNewChallenge.kt)
- Add any required fields
- Set the difficultyLevel property
- Implement the checkAnswer() function with your validation logic
- Override string() to define how the challenge is displayed

### UI (MyNewChallengeScreen.kt)
- Replace the TODO with Compose UI elements to render the challenge and handle user input

Note: At the moment, only numeric-answer challenges are supported.

### Example
