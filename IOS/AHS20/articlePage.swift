//
//  File.swift
//  AHS20
//
//  Created by Richard Wei on 4/21/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class articlePageViewController: UIViewController{
    @IBOutlet weak var backButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad();
    }
    
    @IBAction func exitArticle(_ sender: UIButton) {
        dismiss(animated: true);
    }
}
