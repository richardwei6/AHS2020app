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

class profilePageClass: UIViewController{
    
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    @objc func signOutFunction(){
        print("signed out");
        GIDSignIn.sharedInstance()?.signOut();
    }
    
    func renderView(){
        // set up scroll view here
        
        for view in mainScrollView.subviews{
            view.removeFromSuperview();
        }
        
        let padding = CGFloat(10);
        var scrollViewHeight = CGFloat(0);
        
        let signInButtonHeight = CGFloat(80);
        let signInButton = GIDSignInButton();
        signInButton.center = CGPoint(x: UIScreen.main.bounds.width/2, y: scrollViewHeight + padding + signInButtonHeight);
        signInButton.frame.size.height = signInButtonHeight;
        signInButton.frame.size.width = CGFloat(100);
        
        scrollViewHeight += signInButton.frame.height + padding + signInButtonHeight;
        
        mainScrollView.addSubview(signInButton);
        
        let signOutButtonWidth = CGFloat(100);
        let signOutButtonFrame = CGRect(x: (UIScreen.main.bounds.width/2) - (signOutButtonWidth/2), y: padding + scrollViewHeight, width: signOutButtonWidth, height: 80);
        let signOutButton = UIButton(frame: signOutButtonFrame);
        signOutButton.backgroundColor = UIColor.black;
        signOutButton.layer.cornerRadius = 5;
        
        signOutButton.addTarget(self, action: #selector(self.signOutFunction), for: .touchUpInside);
        
        scrollViewHeight += signOutButtonFrame.height + padding;
        
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
