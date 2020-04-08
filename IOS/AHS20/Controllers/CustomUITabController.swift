//
//  CustomUITabController.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class CustomTabBarController: UITabBarController {

@IBOutlet weak var profileTabBar: UITabBar!

 override func viewDidLoad() {
     super.viewDidLoad()
    // Set the size and the position in the screen of the tab bar
    }
    
    override func viewWillLayoutSubviews() {
        //TabBar.frame = CGRect(x: 0, y: 0, width: TabBar.frame.size.width, height: TabBar.frame.size.height)
        super.viewWillLayoutSubviews()
    }
}
