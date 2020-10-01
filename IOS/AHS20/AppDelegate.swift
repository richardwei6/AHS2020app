//
//  AppDelegate.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import UIKit
import Firebase
import UserNotifications
import FirebaseMessaging
import GoogleSignIn

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, GIDSignInDelegate {
    
    
    var window: UIWindow?;
    let gcmMessageIDKey = "gcm.message_id";
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        FirebaseApp.configure();
        Messaging.messaging().delegate = self;
        if #available(iOS 10.0, *){
            UNUserNotificationCenter.current().delegate = self
            
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: {_, _ in })
        }
        else{
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        application.registerForRemoteNotifications();
        
        
        GIDSignIn.sharedInstance()?.clientID = "654225823864-jq2vdkeeoh3evsi7oun6r5u6li7ie70m.apps.googleusercontent.com";
        GIDSignIn.sharedInstance()?.delegate = self;
        
        return true;
    }
    
    // MARK: FIREBASE START --------
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) { // handle when tapping on notification
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        
        /*print("Tapped on notif");
         
         if let messageID = userInfo[gcmMessageIDKey] {
         print("Message ID: \(messageID)")
         }
         
         // Print full message.
         print(userInfo)*/
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        
        /* print("Tapped on notif2");
         if let messageID = userInfo[gcmMessageIDKey] {
         print("Message ID: \(messageID)")
         }
         
         
         // Print full message.
         print(userInfo)*/
        
        completionHandler(UIBackgroundFetchResult.newData)
    }
    // [END receive_message]
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        //print("Unable to register for remote notifications: \(error.localizedDescription)")
    }
    
    
    // MARK: FIREBASE END --------
    
    // MARK: UISceneSession Lifecycle
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    
    
    // GOOGLE SSO signin
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if let error = error {
            if (error as NSError).code == GIDSignInErrorCode.hasNoAuthInKeychain.rawValue {
                print("The user has not signed in before or they have since signed out.")
            } else {
                print("\(error.localizedDescription)")
            }
            return
        }
        /* Perform any operations on signed in user here.
         let userId = user.userID                  // For client-side use only!
         let idToken = user.authentication.idToken // Safe to send to the server
         let fullName = user.profile.name
         let givenName = user.profile.givenName
         let familyName = user.profile.familyName
         let email = user.profile.email
         // ...*/
        print("signed in - \(user.profile.email)")
        
        isSignedIn = true;
        userEmail = user.profile.email;
        userFullName = user.profile.name;
        
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "reloadProfilePage"), object: nil, userInfo: nil);
        
    }
    
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!,
              withError error: Error!) {
        // Perform any operations when the user disconnects from app here.
        //print("signed out functions");
    }
    
}


// MARK: FIREBASE HANDLING MESSAGES
@available(iOS 10, *)
extension AppDelegate : UNUserNotificationCenterDelegate {
    
    // Receive displayed notifications for iOS 10 devices.
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) { // handle regularly
        //let userInfo = notification.request.content.userInfo
        
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        /*if let messageID = userInfo[gcmMessageIDKey] {
         print("Message ID: \(messageID)")
         }
         print("Handled notifications reg");
         // Print full message.
         print(userInfo)*/
        
        
        // Change this to your preferred presentation option
        completionHandler([[.alert, .sound]])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) { // handle tapping on notification
        let userInfo = response.notification.request.content.userInfo
        // Print message ID.
        
        //   print("tapped on notification");
        
        // Print full message.
        //  print(userInfo["articleID"])
        // TODO: GOTO ARTICLE from  articleID
        //print(userInfo);
        
        findArticleFromIDAndSegue(id: userInfo["articleID"] as? String ?? "");
        completionHandler();
    }
}
// [END ios_10_message_handling]

extension AppDelegate : MessagingDelegate {
    // [START refresh_token]
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
        //   print("Firebase registration token: \(fcmToken)")
        
        selectedNotifications = UserDefaults.standard.array(forKey: "selectedNotifications") as? [Bool] ?? [true, false, false, false, false];
        
        updateSubscriptionNotifs();
        
        let dataDict:[String: String] = ["token": fcmToken]
        NotificationCenter.default.post(name: Notification.Name("FCMToken"), object: nil, userInfo: dataDict)
        // TODO: If necessary send token to application server.
        // Note: This callback is fired at each app startup and whenever a new token is generated.
    }
    // [END refresh_token]
}
