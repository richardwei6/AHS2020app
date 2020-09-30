//
//  profile.swift
//  AHS20
//
//  Created by Richard Wei on 9/27/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import GoogleSignIn

var userEmail = "";
var userFullName = "";
var isSignedIn = false;

class profilePageClass: UIViewController{
    
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    @objc func signOutFunction(){
        print("signed out");
        GIDSignIn.sharedInstance()?.signOut();
        userEmail = "";
        userFullName = "";
        isSignedIn = false;
        renderView();
    }
    
    func isValidStudentEmail(email: String) -> Bool{
        
        return false;
    }
    
    func idFromStudentEmail(email: String) -> Int{ // xxxxx : ex - 30235
        
        return 0;
    }

    
    func renderView(){
        // set up scroll view here
        
        for view in mainScrollView.subviews{
            view.removeFromSuperview();
        }
        
        let padding = CGFloat(10);
        var scrollViewHeight = CGFloat(0);
        
        scrollViewHeight += padding;
        
        let signInButtonHeight = CGFloat(80);
        let signInButton = GIDSignInButton();
        signInButton.center = CGPoint(x: UIScreen.main.bounds.width/2, y: scrollViewHeight + signInButtonHeight);
        signInButton.frame.size.height = signInButtonHeight;
        signInButton.frame.size.width = CGFloat(100);
        
        scrollViewHeight += signInButton.frame.height + signInButtonHeight + padding;
        
        mainScrollView.addSubview(signInButton);
        
        scrollViewHeight += padding;
        
        let signOutButtonWidth = CGFloat(100);
        let signOutButtonFrame = CGRect(x: (UIScreen.main.bounds.width/2) - (signOutButtonWidth/2), y: scrollViewHeight, width: signOutButtonWidth, height: 80);
        let signOutButton = UIButton(frame: signOutButtonFrame);
        signOutButton.backgroundColor = UIColor.black;
        signOutButton.layer.cornerRadius = 5;
        
        signOutButton.addTarget(self, action: #selector(self.signOutFunction), for: .touchUpInside);
        
        scrollViewHeight += signOutButtonFrame.height + padding;
        
        if (isSignedIn){
            let emailLabelWidth = CGFloat(250);
            let emailLabelRect = CGRect(x: (UIScreen.main.bounds.width/2) - (emailLabelWidth/2), y: scrollViewHeight, width: emailLabelWidth, height: 40);
            let emailLabel = UILabel(frame: emailLabelRect);
            emailLabel.text = userEmail;
            emailLabel.backgroundColor = UIColor.gray;
            emailLabel.textColor = UIColor.white;
            emailLabel.textAlignment = .center;
            
            scrollViewHeight += emailLabelRect.height + padding;
        
            let nameLabelWidth = CGFloat(100);
            let nameLabelRect = CGRect(x: (UIScreen.main.bounds.width/2) - (nameLabelWidth/2), y: scrollViewHeight, width: nameLabelWidth, height: 40);
            let nameLabel = UILabel(frame: nameLabelRect);
            nameLabel.text = userFullName;
            nameLabel.backgroundColor = UIColor.gray;
            nameLabel.textColor = UIColor.white;
            nameLabel.textAlignment = .center;
            
            scrollViewHeight += nameLabelRect.height + padding;
            
            mainScrollView.addSubview(nameLabel);
            mainScrollView.addSubview(emailLabel);
        }
        
        mainScrollView.addSubview(signOutButton);
        
        mainScrollView.contentSize = CGSize(width: UIScreen.main.bounds.width, height: scrollViewHeight);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        GIDSignIn.sharedInstance()?.presentingViewController = self;
        GIDSignIn.sharedInstance()?.restorePreviousSignIn();
        
        renderView();
    }
}
