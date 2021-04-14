# NativeAndroidTask
 Native Android app with Kotlin and Firebase. Uses Authentication, Realtime Database & CloudFunctions
 
 ## Features and goals
 
 ### Authentication  
- Login and Register screens. Use email and password. After signin up successfully, show a welcome screen.   
- While log in is in progress, show a waiting indicator. If log in fails, show an alert through a SnackBar.   
- Only Login and Register are available for non-authenticated users.   
- Route control with Navigation component and Directions.  
- Profile page, show the user email on the top and Log Out button.   
- Welcome screen visible only after the first login.  

### Firebase/Firestore   
- In Welcome screen, there is a button to navigate to a List screen.  
- The List screen is a simple list, every list item is a text object only.   
- List items are read only and separated by a horizontal thin line.  
- List items are swipable to delete.  
- At the bottom right of the list screen, there is an Action button to add a new list item. When clicking on the Action button, it shows a dialog asking to enter the text. Text should be at most 40 characters. When typing in, user can see the character count. E.g: if user types in 35 characters, it shows 35/40 on the bottom right and bellow the text input.   
- This list is connected directly to the Firebase database under the user’s data node.   

### Cloud functions: 
- There is a cloud function that listens to a list item above and:  
 . Capitalizes the first letter  
 . Trims spaces around.  
 . Removes continuous spaces between words.  
 
### Additional functionality: 
- The list screen has a Profile button on the top-right. This Profile button redirects the user to the profile page.   
- Auto-login implemented. If the user is logged in, app moves to the home page automatically.
