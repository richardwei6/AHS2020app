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


class CustomTabBarController: UIViewController, UIViewControllerTransitioningDelegate {
    
    @IBOutlet weak var contentView: UIView!
    
    @IBOutlet weak var tabBarView: UIView!
    
    
    @IBOutlet weak var notificationDot: UIView!
    @IBOutlet weak var notificationButton: UIButton!
    @IBOutlet var buttons: [UIButton]!
    
    @IBOutlet weak var topBar: UIView!
    @IBOutlet weak var topBarHeightContraint: NSLayoutConstraint!
    
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
    
    //    let homeTopCornerRadius = CGFloat(15);
    
    let iconImagePath = ["home", "bulletin", "saved", "", "settings"];
    let selectedColor = makeColor(r: 243, g: 149, b: 143);
    
    var articleContentInSegue: articleData?;
    
    @IBAction func openNotifications(_ sender: UIButton) {
        //print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }
    
    
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
    
    func setUpNotifDot(){
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
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        setUpConnection();
        setUpNotifDot();
        
        // DEVELOPER ONLY FUNC
        setUpDevConfigs();
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.articleSelector), name:NSNotification.Name(rawValue: "article"), object: nil);

        savedArticleClass.getArticleDictionary();
        
        fontSize = UserDefaults.standard.integer(forKey: "fontSize") != 0 ? UserDefaults.standard.integer(forKey: "fontSize") : 20;
        
        
        // set up buttons
        for index in 0..<buttons.count{
            let image = UIImage(named: iconImagePath[index]);
            buttons[index].setImage(image, for: .normal);
            buttons[index].tintColor = UIColor.gray;
            buttons[index].imageView?.contentMode = .scaleAspectFit;
            buttons[index].contentVerticalAlignment = .fill;
            buttons[index].contentHorizontalAlignment = .fill;
        }
        
        
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
        tabBarView.layer.shadowColor = UIColor.gray.cgColor;
        tabBarView.layer.shadowOpacity = 0.07;
        tabBarView.layer.shadowRadius = 5;
        tabBarView.layer.shadowOffset = CGSize(width: 0, height: -10);
        buttons[0].tintColor = UIColor.black;
        dateLabel.text = getTitleDateAndMonth();
    }
    
    override func viewDidAppear(_ animated: Bool) {
        // set notification dot
        notificationDot.isHidden = unreadNotifCount == 0;
    }
    
    @IBAction func didPressTab(_ sender: UIButton) {
        let prevIndex = selectedIndex;
        selectedIndex = sender.tag;
        if (prevIndex == sender.tag){
            if (sender.tag == 0){
                // add code here
                if let page  = viewControllers[0] as? homeClass{
                    page.mainScrollView.setContentOffset(.zero, animated: true);
                }
                
            }
            if (sender.tag == 2){
                if let page = viewControllers[sender.tag] as? savedClass{
                    page.mainScrollView.setContentOffset(.zero, animated: true);
                }
            }
            if (sender.tag == 1){
                if let page = viewControllers[sender.tag] as? bulletinClass{
                    page.bulletinScrollView.setContentOffset(.zero, animated: true);
                }
            }
        }
        else{
            // remove prev view controller
            buttons[prevIndex].tintColor = UIColor.gray;
            
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


