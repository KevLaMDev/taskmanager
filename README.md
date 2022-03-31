# TaskMaster

## Description
- TaskMaster is an Android app that allows users to keep track of their tasks.

## Change Log
- 3/21:
    - Added three basic activity pages: designed general layout and Intent to move between pages
    - Screenshots:
      ![image](screenshots/Screenshot1.png)
      ![image](screenshots/Screenshot2.png)
      ![image](screenshots/Screenshot3.png)
      
- 3/22: 
    - Changed home page of app: now has three different task buttons, each leading to a task details activity. 
    - Data is passed from the home screen to each Task Details instance.
    - Added a settings activity which is accessible from the home screen. The user can save a username which is referencable through out the rest of the app via Shared preferences
    - Screenshots:
      ![New home screen](screenshots/Screenshot3.png)
      ![Task Details page](screenshots/Screenshot4.png)
      
- 3/23: 
    - Changed home page of app: now has a recycler view list of tasks which are currently hard coded in.
    - Each task on the recycler list takes the user to the Task Details activity, passing the name of the task
    - Added a Task object with an enum property to represent the completion status of tasks
    - Screenshots:
     ![New Home screen](screenshots/Screenshot5.png)
      
- 3/24:
    - Added sqlite database to persist data
    
- 3/29:
    - Removed Room DB and added AWS amplify
    
- 3/31:
    - Added team model with appropriate relationship to task models
