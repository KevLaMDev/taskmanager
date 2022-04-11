// String emptyFileName = "emptyFileName";
//        File emptyFile = new File(getApplicationContext().getFilesDir(), emptyFileName);
//        try
//        {
//            BufferedWriter fileBufferedWriter = new BufferedWriter(new FileWriter(emptyFile));
//            fileBufferedWriter.append("test here\nanother test");
//            fileBufferedWriter.close();
//        }
//        catch (IOException ioException){
//            Log.e(TAG, "could not write file locally: " + emptyFileName);
//        }
//
//        String emptyFileS3Key = "someFileOnS3";
//
//        Amplify.Storage.uploadFile(
//                emptyFileS3Key,
//                emptyFile,
//                result -> Log.i(TAG, "Successfully uploaded: " + result.getKey()),
//                storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
//        );

        // start by manually creating a new user
//        Amplify.Auth.signUp("lamarca.k@gmail.com",
//                "totallySecureP@ssword",
//                AuthSignUpOptions.builder()
//                        .userAttribute(AuthUserAttributeKey.email(), "lamarca.k@gmail.com")
//                        .userAttribute(AuthUserAttributeKey.nickname(), "Kev")
//                        .build(),
//                    good ->
//                    {
//                        Log.i(TAG, "Signup success: " + good.toString());
//                    },
//                    bad ->
//                    {
//                        Log.i(TAG, "signup failed");
//                    }
//        );
// Next we need to verify that user

//        Amplify.Auth.confirmSignUp("lamarca.k@gmail.com",
//                    "216580",
//                    success ->
//                    {
//                        Log.i(TAG, "Verification success");
//                    },
//                    failure ->
//                    {
//                        Log.i(TAG, "Verification failed");
//                    });
// Then log in as that user

//        Amplify.Auth.signIn("lamarca.k@gmail.com",
//                "totallySecureP@ssword",
//                success ->
//                {
//                    Log.i(TAG, "Login success");
//                },
//                failure ->
//                {
//                    Log.i(TAG, "Login failed");
//                }
//        );

        // Log out

//        Amplify.Auth.signOut(
//                () -> {Log.i(TAG, "Logout success"); },
//                failure -> {Log.i(TAG, "Logout failed");}
//        );

//      DB proof of life:

//        Team team1 =
//                Team.builder().name("Team 1").build();
//        Task task1 =
//                Task.builder().title("task1").body("body").team(team1).build();
//        Amplify.API.mutate(
//                ModelMutation.create(task1),
//                successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a task successfully"),
//                    failureResponse -> Log.i(TAG, "MainActivity.onCreate(): failed with " + failureResponse)
//                );
//
//        Amplify.API.mutate(ModelMutation.create(team1),
//                    successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a task successfully"),
//                    failureResponse -> Log.i(TAG, "MainActivity.onCreate(): failed with " + failureResponse)
//                );
//        Team team2 =
//                Team.builder().name("Team 2").build();
//        Amplify.API.mutate(ModelMutation.create(team2),
//                successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a task successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): failed with " + failureResponse)
//        );
//        Team team3 =
//                Team.builder().name("Team 3").build();
//        Amplify.API.mutate(ModelMutation.create(team3),
//                successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a task successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): failed with " + failureResponse)

//        );