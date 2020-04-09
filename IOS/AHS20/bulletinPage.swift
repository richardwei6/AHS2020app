//
//  bulletinPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class bulletinClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {
    @IBOutlet weak var seniorButton: UIButton!
    @IBOutlet weak var collegeButton: UIButton!
    @IBOutlet weak var athleticsButton: UIButton!
    @IBOutlet weak var eventsButton: UIButton!
    @IBOutlet weak var referenceButton: UIButton!
    @IBOutlet weak var otherButton: UIButton!
    
    var buttonTextEdgeInset = UIEdgeInsets(top: 0, left: 50, bottom: 0, right: 0);
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        // set up correct spacing between text on button and image
        seniorButton.contentEdgeInsets = buttonTextEdgeInset;
        seniorButton.contentHorizontalAlignment = .left;
        collegeButton.contentEdgeInsets = buttonTextEdgeInset;
        collegeButton.contentHorizontalAlignment = .left;
        athleticsButton.contentEdgeInsets = buttonTextEdgeInset;
        athleticsButton.contentHorizontalAlignment = .left;
        eventsButton.contentEdgeInsets = UIEdgeInsets(top: 0, left: 55, bottom: 0, right: 0);
        eventsButton.contentHorizontalAlignment = .left;
        referenceButton.contentEdgeInsets = UIEdgeInsets(top: 0, left: 45, bottom: 0, right: 0);
        referenceButton.contentHorizontalAlignment = .left;
        otherButton.contentEdgeInsets = buttonTextEdgeInset;
        otherButton.contentHorizontalAlignment = .left;
        seniorButton.layer.cornerRadius = 14;
        collegeButton.layer.cornerRadius = 14;
        athleticsButton.layer.cornerRadius = 14;
        eventsButton.layer.cornerRadius = 14;
        referenceButton.layer.cornerRadius = 14;
        otherButton.layer.cornerRadius = 14;
        
        
        
        
    }

}
