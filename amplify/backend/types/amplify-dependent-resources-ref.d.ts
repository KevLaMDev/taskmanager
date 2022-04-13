export type AmplifyDependentResourcesAttributes = {
    "api": {
        "amplifyDatasource": {
            "GraphQLAPIKeyOutput": "string",
            "GraphQLAPIIdOutput": "string",
            "GraphQLAPIEndpointOutput": "string"
        }
    },
    "auth": {
        "taskmastera177dcae": {
            "IdentityPoolId": "string",
            "IdentityPoolName": "string",
            "UserPoolId": "string",
            "UserPoolArn": "string",
            "UserPoolName": "string",
            "AppClientIDWeb": "string",
            "AppClientID": "string"
        }
    },
    "storage": {
        "taskmasterstorage": {
            "BucketName": "string",
            "Region": "string"
        }
    },
    "analytics": {
        "taskmaster": {
            "Region": "string",
            "Id": "string",
            "appName": "string"
        }
    },
    "predictions": {
        "speechGenerator5c3eeaec": {
            "region": "string",
            "language": "string",
            "voice": "string"
        },
        "translateText49f5942a": {
            "region": "string",
            "sourceLang": "string",
            "targetLang": "string"
        }
    }
}