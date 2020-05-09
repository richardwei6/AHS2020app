//
//  creditsPage.swift
//  AHS20
//
//  Created by Richard Wei on 5/8/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit


extension creditsPage: CAAnimationDelegate {
    func animationDidStop(_ anim: CAAnimation, finished flag: Bool) {
        if flag {
            gradient.colors = gradientSet[currentgradient]
            animateGradient()
        }
    }
}

class creditsPage: UIViewController {
    
    @IBOutlet var mainView: UIView!
    let gradient = CAGradientLayer();
    
    var gradientSet = [[CGColor]]();
    var currentgradient: Int = 0;
    
    let one = makeColor(r: 0, g: 200, b: 255).cgColor; // light blue
    let two = makeColor(r: 0, g: 128, b: 255).cgColor; // blue
    let three = makeColor(r: 255, g: 0, b: 89).cgColor; // pink
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        gradientSet.append([one, two])
        gradientSet.append([two, three])
        gradientSet.append([three, one])
        
        
        gradient.frame = self.view.bounds
        gradient.colors = gradientSet[currentgradient]
        gradient.startPoint = CGPoint(x:0, y:0)
        gradient.endPoint = CGPoint(x:1, y:1)
        gradient.drawsAsynchronously = true
        mainView.layer.addSublayer(gradient)
        
        animateGradient()
    }
    
    func animateGradient() {
       // print(currentgradient)
        if currentgradient < gradientSet.count - 1 {
            currentgradient += 1
        } else {
            currentgradient = 0
        }
        
        let gradientChangeAnimation = CABasicAnimation(keyPath: "colors")
        gradientChangeAnimation.duration = 2.0
        gradientChangeAnimation.toValue = gradientSet[currentgradient]
        gradientChangeAnimation.fillMode = .forwards
        gradientChangeAnimation.isRemovedOnCompletion = false
            gradientChangeAnimation.delegate = self;
        gradient.add(gradientChangeAnimation, forKey: "colorChange")
    }
    
    @IBAction func exitCredits(_ sender: Any) {
        dismiss(animated: true);
    }
    
    
}
