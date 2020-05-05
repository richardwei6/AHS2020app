//
//  CustomUITabController.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class CustomTabBarController: UIViewController {

    @IBOutlet weak var contentView: UIView!
    
    @IBOutlet weak var tabBarView: UIView!
    
    @IBOutlet var buttons: [UIButton]!
    
    var homeViewController: UIViewController!
    var bulletinViewController: UIViewController!
    var savedViewController: UIViewController!
    var settingsViewController: UIViewController!
    
    var viewControllers: [UIViewController]!
    
    var selectedIndex: Int = 0;

    let iconImagePath = ["house.fill", "doc.plaintext", "bookmark", "gear"];
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        contentView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        tabBarView.bottomAnchor.constraint(equalTo: self.view.bottomAnchor, constant: -25).isActive = true;
        tabBarView.layer.cornerRadius = 30;
       // tabBarView.frame.size.height = CGFloat(70);
        

        // set up buttons
        for index in 0..<buttons.count{
            var image = UIImage(systemName: iconImagePath[index]);
            image = image?.maskWithColor(color: UIColor.white);
            buttons[index].setImage(image, for: .normal);
            buttons[index].tintColor = UIColor.white;
        }
        
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil);
        homeViewController = storyboard.instantiateViewController(withIdentifier: "homeViewController");
        bulletinViewController = storyboard.instantiateViewController(withIdentifier: "bulletinViewController");
        savedViewController = storyboard.instantiateViewController(withIdentifier: "savedViewController");
        settingsViewController = storyboard.instantiateViewController(withIdentifier: "settingsViewController");
        viewControllers = [homeViewController, bulletinViewController, savedViewController, settingsViewController];
        buttons[selectedIndex].isSelected = true;
        didPressTab(buttons[selectedIndex]);
    }
    
    @IBAction func didPressTab(_ sender: UIButton) {
        let prevIndex = selectedIndex;
        selectedIndex = sender.tag;
        
        // remove prev view controller
        buttons[prevIndex].isSelected = false;
        let prevVC = viewControllers[prevIndex];
        prevVC.willMove(toParent: nil);
        prevVC.view.removeFromSuperview();
        prevVC.removeFromParent();
        
        // add current view controller
        sender.isSelected = true;
        let vc = viewControllers[selectedIndex];
        addChild(vc);
        vc.view.frame = contentView.bounds;
        contentView.addSubview(vc.view);
        vc.didMove(toParent: self);
    }
    
    
}
