//
//  CustomUITabController.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import Firebase
import GoogleSignIn

class CustomTabBarController: UIViewController, UIViewControllerTransitioningDelegate {
    
    
    @IBOutlet weak var mainContentView: UIView!
    
    @IBOutlet var outerView: UIView!
    @IBOutlet weak var contentView: UIView!
    
    @IBOutlet var outerViewGestureRecognizer: UIPanGestureRecognizer!
    //@IBOutlet weak var tabBarView: UIView!
    
    
 //   @IBOutlet weak var notificationDot: UIView!
 //   @IBOutlet weak var notificationButton: UIButton!
    //@IBOutlet var buttons: [UIButton]!
    @IBOutlet weak var hamBurgMenuButton: UIButton!
    

    
    @IBOutlet weak var hamBurgMenuView: UIView!
    
    @IBOutlet weak var topBar: UIView!
    @IBOutlet weak var topBarHeightContraint: NSLayoutConstraint!
    
    @IBOutlet weak var mainContentViewWidthConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var hamBurgMenuLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var hamBurgMenuWidthConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var topBarPageName: UILabel!
    @IBOutlet weak var homeTopBarContent: UIView!
    @IBOutlet weak var dateLabel: UILabel!
    
    @IBOutlet weak var featherImage: UIImageView!
    
    var homeViewController: UIViewController!
    var bulletinViewController: UIViewController!
    var savedViewController: UIViewController!
    var profileViewController: UIViewController!
    var settingsViewController: UIViewController!
    
    
    var viewControllers: [UIViewController]!
    
    var selectedIndex: Int = 0;
    
    var hamBurgMenuWidth = CGFloat(320);
    //    let homeTopCornerRadius = CGFloat(15);
    
    let iconImagePath = ["home", "bulletin", "", "saved", "settings"]; // use system bell - bell.fill
    let buttonNameArray = ["Home", "Bulletin", "Notifications", "Saved", "Settings"];
    let selectedColor = makeColor(r: 243, g: 149, b: 143);
    
    var articleContentInSegue: articleData?;
    
    /*@IBAction func openNotifications(_ sender: UIButton) {
        //print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }*/
    
    
    var enableHamBurgMenu = false;

    // ARTICLE PAN
    
    let interactor = Interactor();
    let transition = CATransition();
    
    func transition(to controller: UIViewController) {
        transition.duration = 0.2
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromRight
        view.window?.layer.add(transition, forKey: kCATransition)
        present(controller, animated: false)
    }
    
    func animationController(
        forDismissed dismissed: UIViewController)
        -> UIViewControllerAnimatedTransitioning? {
            return DismissAnimator()
    }
    
    func interactionControllerForDismissal(
        using animator: UIViewControllerAnimatedTransitioning)
        -> UIViewControllerInteractiveTransitioning? {
            
            return interactor.hasStarted
                ? interactor
                : nil
    }

    @objc func articleSelector(notification: NSNotification){ // instigate transition
        guard let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(identifier: "articlePageController") as? articlePageViewController else{
            return;
        };
        vc.transitioningDelegate = self;
        vc.interactor = interactor;
        vc.articleContent = notification.userInfo?["articleContent"] as? articleData;
        transition(to: vc);
    }
    
    // ARTICLE PAN END
    
    /*func setUpNotifDot(){
        loadNotifPref();
        selectedNotifications = UserDefaults.standard.array(forKey: "selectedNotifications") as? [Bool] ?? [true, false, false, false, false];
        updateSubscriptionNotifs();
        unreadNotifCount = 0;
        setUpConnection();
        if (internetConnected){
            totalNotificationList = [notificationData]();
            ref.child("notifications").observeSingleEvent(of: .value) { (snapshot) in
                let enumerator = snapshot.children;
                while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                    let enumerator = article.children;
                    var singleNotification = notificationData();
                    
                    singleNotification.messageID =  article.key;
                    
                    while let notificationContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                        
                        if (notificationContent.key == "notificationArticleID"){
                            singleNotification.notificationArticleID  = notificationContent.value as? String;
                        }
                        else if (notificationContent.key == "notificationBody"){
                            singleNotification.notificationBody  = notificationContent.value as? String;
                        }
                        else if (notificationContent.key == "notificationTitle"){
                            singleNotification.notificationTitle  = notificationContent.value as? String;
                        }
                        else if (notificationContent.key == "notificationUnixEpoch"){
                            singleNotification.notificationUnixEpoch  = notificationContent.value as? Int64;
                        }
                        else if (notificationContent.key == "notificationCategory"){
                            singleNotification.notificationCatagory = notificationContent.value as? Int;
                        }
                        
                    }
                    totalNotificationList.append(singleNotification);
                    if ((selectedNotifications[0] == true || selectedNotifications[singleNotification.notificationCatagory ?? 0] == true || singleNotification.notificationCatagory == 0) && notificationReadDict[singleNotification.messageID ?? ""] != true){
                        unreadNotifCount += 1;
                    }
                    self.notificationDot.isHidden = unreadNotifCount == 0;
                    UIApplication.shared.applicationIconBadgeNumber = unreadNotifCount;
                };
            }
        }
    }*/
    
    func openHamBurgMenu(){
        enableHamBurgMenu = true;
        UIView.animate(withDuration: 0.2){
            self.hamBurgMenuLeadingConstraint.constant = 0;
            self.outerView.layoutIfNeeded();
        }
    }
    
    func closeHamBurgMenu(){
        enableHamBurgMenu = false;
        UIView.animate(withDuration: 0.2){
            self.hamBurgMenuLeadingConstraint.constant = -self.hamBurgMenuWidth; // const
            self.outerView.layoutIfNeeded();
        }
    }
    
    @IBAction func toggleHamBurgMenu(_ sender: UIButton) {
        if (enableHamBurgMenu){
            closeHamBurgMenu();
        }
        else{
            openHamBurgMenu();
        }
        //print("enableHamBurgMenu - \(enableHamBurgMenu)")
    }
    
    @objc func renderHamBurgMenu(){
        for view in hamBurgMenuView.subviews{
            view.removeFromSuperview();
        }

        let contentScrollView = UIScrollView(frame: CGRect(x: 0, y: 0, width: hamBurgMenuWidth, height: hamBurgMenuView.frame.height));
        
        let padding = CGFloat(20);
        var nextY = CGFloat(0);
        
        nextY += 45;
    
        /// START PROFILE BUTTON
        
        let profilePicSize = CGFloat(80); // 80
        
        let profileVerticalPadding = CGFloat(20);
        let profileHorizontalPadding = CGFloat(15);
        
        /// Attributes to determine height
        //let profileTextContent = isSignedIn ? userFullName : "text text text text text text text text text text text text text text text text text text text text text text text text text text text text text";
        let profileTextContent = isSignedIn ? userFullName : "Sign in";
        let profileTextWidth = CGFloat(hamBurgMenuWidth - 3*profileHorizontalPadding - profilePicSize);
        let profileTextFont = UIFont(name: "SFProDisplay-Semibold", size: 30);
        
        let profileManageText = "View Profile >";
        let profileManageTextFont = UIFont(name: "SFProDisplay-Semibold", size: 15);
        let profileManageTextOffset = CGFloat(2);
        
        let profileButtonHeight = CGFloat(10 + profileVerticalPadding + profileTextContent.getHeight(withConstrainedWidth: profileTextWidth, font: profileTextFont!) + profileManageText.getHeight(withConstrainedWidth: profileTextWidth - profileManageTextOffset, font: profileManageTextFont!) + 16 + profileVerticalPadding);
        
        /// end attributes
        
        let profilePicViewFrame = CGRect(x: profileHorizontalPadding, y: (profileButtonHeight/2) - (profilePicSize/2), width: profilePicSize, height: profilePicSize);
        let profilePicView = UIImageView(frame: profilePicViewFrame);
        
        if (isSignedIn){
            profilePicView.imgFromURL(sURL: userProfileImageURL);
        }
        else{
            profilePicView.image = UIImage(named: "defaultprofileimage");
        }
        
        profilePicView.clipsToBounds = true;
        profilePicView.layer.cornerRadius = profilePicSize/2;
        profilePicView.contentMode = .scaleAspectFill;
        profilePicView.layer.borderWidth = 1;
        profilePicView.layer.borderColor = UIColor.black.cgColor;
        

        let profileTextFrame = CGRect(x: 2*profileHorizontalPadding + profilePicSize, y: profileVerticalPadding + 10, width: profileTextWidth, height: profileTextContent.getHeight(withConstrainedWidth: profileTextWidth, font: profileTextFont!));
        let profileText = UILabel(frame: profileTextFrame);
        profileText.text = profileTextContent;
        profileText.font = profileTextFont;
        profileText.numberOfLines = 0;
       // profileText.backgroundColor = UIColor.gray;
        
    
        let profileManageTextFrame = CGRect(x: 2*profileHorizontalPadding + profilePicSize + profileManageTextOffset, y: profileTextFrame.maxY, width: profileTextFrame.width - profileManageTextOffset, height: profileManageText.getHeight(withConstrainedWidth: profileTextFrame.width - profileManageTextOffset, font: profileManageTextFont!));
        let profileManageTextLabel = UILabel(frame: profileManageTextFrame);
        profileManageTextLabel.text = profileManageText;
        profileManageTextLabel.font = profileManageTextFont;
        profileManageTextLabel.textColor = UIColor.gray;
        profileManageTextLabel.numberOfLines = 0;
        
        //print("max y - \(profileManageTextLabel.frame.maxY)") // add 16 to vertical padding get to 120
        
        let profileButtonFrame = CGRect(x: 0, y: nextY, width: hamBurgMenuWidth, height: profileButtonHeight);
        let profileButton = UIButton(frame: profileButtonFrame);
        
        // PROFILE Button subviews
        profileButton.addSubview(profileText);
        profileButton.addSubview(profileManageTextLabel);
        profileButton.addSubview(profilePicView);
        
        //profileButton.layer.borderColor = UIColor.lightGray.cgColor;
        //profileButton.layer.borderWidth = 1;
        
        profileButton.tag = 3;
        
        profileButton.addTarget(self, action: #selector(self.didPressTab), for: .touchUpInside);
        
        contentScrollView.addSubview(profileButton);
        /// END PROFILE BUTTON
        
        let borderLineFrame = CGRect(x: 0, y: profileButtonFrame.maxY, width: hamBurgMenuWidth, height: 1);
        let borderLine = UIView(frame: borderLineFrame);
        borderLine.backgroundColor = UIColor.lightGray;
        
        contentScrollView.addSubview(borderLine);
        
        let hamBurgMenuButtonVerticalPadding = CGFloat(10);
        
        nextY += profileButtonFrame.height + hamBurgMenuButtonVerticalPadding;

        let hamBurgMenuButtonHorizontalPadding = CGFloat(10);
        let hamBurgMenuButtonHeight = CGFloat(70);
        
        for i in 0...4{
            
            let buttonFrame = CGRect(x: hamBurgMenuButtonHorizontalPadding, y: nextY, width: hamBurgMenuWidth - 2*hamBurgMenuButtonHorizontalPadding, height: hamBurgMenuButtonHeight);
            let button = UIButton(frame: buttonFrame);
            //button.backgroundColor = UIColor.gray;
            
            let buttonImagePadding = CGFloat(15);
            let buttonImageFrame = CGRect(x: buttonImagePadding, y: buttonImagePadding, width: hamBurgMenuButtonHeight - 2*buttonImagePadding, height: hamBurgMenuButtonHeight - 2*buttonImagePadding);
            let buttonImage = UIImageView(frame: buttonImageFrame);
            
            if (i == 2){ // use bell.fill system image
                buttonImage.image = UIImage(systemName: "bell.fill");
            }
            else{
                buttonImage.image = UIImage(named: iconImagePath[i]);
            }
            
            buttonImage.contentMode = .scaleAspectFit;
            buttonImage.tintColor = UIColor.black;
            
            button.addSubview(buttonImage);
            
            let buttonNameFrame = CGRect(x: buttonImageFrame.maxX + 15, y: buttonImagePadding, width: buttonFrame.width - buttonImageFrame.maxX - 15, height: hamBurgMenuButtonHeight - 2*buttonImagePadding);
            let buttonName = UILabel(frame: buttonNameFrame);
            buttonName.text = buttonNameArray[i];
            buttonName.font = UIFont(name: "SFProDisplay-Semibold", size: 22);
            buttonName.lineBreakMode = .byTruncatingTail;
            buttonName.numberOfLines = 1;
            
            button.addSubview(buttonName);
            
            contentScrollView.addSubview(button);
            
            nextY += buttonFrame.height + hamBurgMenuButtonVerticalPadding;
        }
        
        contentScrollView.contentSize = CGSize(width: hamBurgMenuWidth, height: nextY);
        //contentScrollView.backgroundColor = UIColor.gray;
        
        //contentScrollView.layer.borderColor = UIColor.lightGray.cgColor;
        //contentScrollView.layer.borderWidth = 1;
        
        hamBurgMenuView.addSubview(contentScrollView);
        
        let exitButtonFrame = CGRect(x: 10, y: 10, width: 40, height: 40);
        let exitButton = UIButton(frame: exitButtonFrame);
        exitButton.setTitle("X", for: .normal);
        exitButton.setTitleColor(UIColor.black, for: .normal);
        
        exitButton.addTarget(self, action: #selector(self.toggleHamBurgMenu), for: .touchUpInside);
        
        hamBurgMenuView.layer.masksToBounds = false;
        hamBurgMenuView.layer.shadowRadius = 2;
        hamBurgMenuView.layer.shadowOpacity = 0.5;
        hamBurgMenuView.layer.shadowColor = UIColor.lightGray.cgColor;
        hamBurgMenuView.layer.shadowOffset = CGSize(width: 3, height: 3);
        
        hamBurgMenuView.addSubview(exitButton); // NOTE BUTTON IS NOT PART OF SCROLLVIEW
        
    }
    
    @IBAction func handlePan(_ sender: UIPanGestureRecognizer) { // main goal is to set the hamburgmenuconstraint
        let percentThreshold: CGFloat = 0.3;
        let sensitivity = CGFloat(5); // 0 to x, where 1 is normal multiplier
        let translation = sender.translation(in: view);
        let fingerMovement = translation.x / view.bounds.width;
        if (enableHamBurgMenu){ // exit menu
            let rightMovement = fmaxf(Float(-fingerMovement), 0.0);
            let rightMovementPercent = fminf(rightMovement, 1.0);
            let progress = CGFloat(rightMovementPercent);
            if (sender.state == .began || sender.state == .changed){
                //print("exit pan - \(progress)");
                if (progress == 1){
                    closeHamBurgMenu();
                }
                else{
                    UIView.animate(withDuration: 0.2){
                        self.hamBurgMenuLeadingConstraint.constant = -(self.hamBurgMenuWidth * min(progress * sensitivity, 1.0));
                        self.outerView.layoutIfNeeded();
                    }
                }
            }
            else if (sender.state == .ended){
                //print("stop exit pan - \(progress)");
                if (progress > percentThreshold){
                    closeHamBurgMenu();
                }
                else{
                    openHamBurgMenu();
                }
            }
        }
        else{ // open menu
            let leftMovement = fminf(Float(fingerMovement), 1.0);
            let leftMovementPercent = fmaxf(leftMovement, 0.0);
            let progress = CGFloat(leftMovementPercent);
            if (sender.state == .began || sender.state == .changed){
                //print("start pan - \(progress)");
                if (progress == 1){
                    openHamBurgMenu();
                }
                else{
                    UIView.animate(withDuration: 0.2){
                        self.hamBurgMenuLeadingConstraint.constant = -self.hamBurgMenuWidth + (self.hamBurgMenuWidth * min(progress * sensitivity, 1.0));
                        self.outerView.layoutIfNeeded();
                    }
                }
            }
            else if (sender.state == .ended){
                //print("stop start pan - \(progress)");
                if (progress > percentThreshold){
                    openHamBurgMenu();
                }
                else{
                    closeHamBurgMenu();
                }
            }
        }
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        if let scrollView = otherGestureRecognizer.view as? UIScrollView {
            return scrollView.contentOffset.x == 0;
        }
        return false
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
        setUpConnection();
        //setUpNotifDot();
        
        // DEVELOPER ONLY FUNC
        setUpDevConfigs();
        
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.articleSelector), name:NSNotification.Name(rawValue: "article"), object: nil);

        savedArticleClass.getArticleDictionary();
        
        fontSize = UserDefaults.standard.integer(forKey: "fontSize") != 0 ? UserDefaults.standard.integer(forKey: "fontSize") : 20;
        
        
        contentView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        mainContentView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        mainContentViewWidthConstraint.constant = UIScreen.main.bounds.width;
    
        GIDSignIn.sharedInstance()?.restorePreviousSignIn();
        
        hamBurgMenuWidth = UIScreen.main.bounds.width - 80;
        
        hamBurgMenuWidthConstraint.constant = hamBurgMenuWidth;
        hamBurgMenuLeadingConstraint.constant = -hamBurgMenuWidth;
        
        hamBurgMenuView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;

        renderHamBurgMenu();
        // set up buttons
        /*for index in 0..<buttons.count{
            let image = UIImage(named: iconImagePath[index]);
            buttons[index].setImage(image, for: .normal);
            buttons[index].tintColor = UIColor.gray;
            buttons[index].imageView?.contentMode = .scaleAspectFit;
            buttons[index].contentVerticalAlignment = .fill;
            buttons[index].contentHorizontalAlignment = .fill;
        }*/
        
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil);
        homeViewController = storyboard.instantiateViewController(withIdentifier: "homeViewController") as! homeClass;
        bulletinViewController = storyboard.instantiateViewController(withIdentifier: "bulletinViewController") as! bulletinClass;
        savedViewController = storyboard.instantiateViewController(withIdentifier: "savedViewController") as! savedClass;
        profileViewController = storyboard.instantiateViewController(identifier: "profileViewController") as! profilePageClass;
        settingsViewController = storyboard.instantiateViewController(withIdentifier: "settingsViewController") as! settingClass;
        viewControllers = [homeViewController, bulletinViewController, savedViewController, profileViewController, settingsViewController];
        //buttons[selectedIndex].setImage(UIImage(named: iconImagePathInv[selectedIndex]), for: .normal);
        //buttons[selectedIndex].tintColor = selectedColor;
        let vc = viewControllers[selectedIndex];
        addChild(vc);
        vc.view.frame = contentView.bounds;
        contentView.addSubview(vc.view);
        vc.didMove(toParent: self);
        topBar.layer.shadowColor = UIColor.gray.cgColor;
        topBar.layer.shadowOpacity = 0.08;
        topBar.layer.shadowRadius = 5;
        topBar.layer.shadowOffset = CGSize(width: 0 , height:10);
        /*tabBarView.layer.shadowColor = UIColor.gray.cgColor;
        tabBarView.layer.shadowOpacity = 0.07;
        tabBarView.layer.shadowRadius = 5;
        tabBarView.layer.shadowOffset = CGSize(width: 0, height: -10);
        buttons[0].tintColor = UIColor.black;*/
        dateLabel.text = getTitleDateAndMonth();
    }
    
    /*override func viewDidAppear(_ animated: Bool) {
        // set notification dot
       // notificationDot.isHidden = unreadNotifCount == 0;
    }*/
    
    /*
     original order of UIButton.tag is:
     0 == home
     1 == bulletin
     2 == saved
     3 == profile
     4 == settings
     // NEW - 5 - notifications
     */
    
    @objc func didPressTab(_ sender: UIButton) {
        let prevIndex = selectedIndex;
        selectedIndex = sender.tag;
        if (prevIndex == sender.tag){
            if (sender.tag == 0){
                // add code here
                if let page  = viewControllers[0] as? homeClass{
                    page.mainScrollView.setContentOffset(.zero, animated: true);
                }
                
            }
            else if (sender.tag == 2){
                if let page = viewControllers[sender.tag] as? savedClass{
                    page.mainScrollView.setContentOffset(.zero, animated: true);
                }
            }
            else if (sender.tag == 1){
                if let page = viewControllers[sender.tag] as? bulletinClass{
                    page.bulletinScrollView.setContentOffset(.zero, animated: true);
                }
            }
        }
        else{
            // remove prev view controller
            //buttons[prevIndex].tintColor = UIColor.gray;
            
            let prevVC = viewControllers[prevIndex];
            prevVC.willMove(toParent: nil);
            prevVC.view.removeFromSuperview();
            prevVC.removeFromParent();
            
            // add current view controller
            sender.tintColor = UIColor.black;
            //sender.setImage(UIImage(named: iconImagePathInv[sender.tag]), for: .normal);
            let vc = viewControllers[selectedIndex];
            addChild(vc);
            vc.view.frame = contentView.bounds;
            contentView.addSubview(vc.view);
            vc.didMove(toParent: self);
            
            
            if (sender.tag == 0){
                //               topBar.layer.cornerRadius = homeTopCornerRadius;
                //topBar.layer.maskedCorners = [.layerMaxXMaxYCorner, .layerMinXMaxYCorner];
                topBarHeightContraint.constant = 62;
                topBar.layer.shadowColor = UIColor.gray.cgColor;
                homeTopBarContent.isHidden = false;
                topBarPageName.isHidden = true;
            }
            else{
                topBar.layer.cornerRadius = 0;
                topBar.layer.maskedCorners = [.layerMaxXMaxYCorner, .layerMinXMaxYCorner];
                topBarHeightContraint.constant = 60;
                homeTopBarContent.isHidden = true;
                topBarPageName.isHidden = false;
                if (sender.tag == 1){
                    topBarPageName.text = "Student Bulletin";
                    topBar.layer.shadowColor = UIColor.white.cgColor;
                }
                else if (sender.tag == 2){
                    topBarPageName.text = "Saved";
                    topBar.layer.shadowColor = UIColor.gray.cgColor;
                }
                else if (sender.tag == 3){
                    topBarPageName.text = "Profile";
                    topBar.layer.shadowColor = UIColor.gray.cgColor;
                }
                else if (sender.tag == 4){
                    topBarPageName.text = "Settings";
                    topBar.layer.shadowColor = UIColor.gray.cgColor;
                }
            }
            
        }
        
    }
    
    
}


