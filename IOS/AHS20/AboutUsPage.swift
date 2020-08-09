//
//  creditsPage.swift
//  AHS20
//
//  Created by Richard Wei on 5/8/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit


extension aboutUsPage: CAAnimationDelegate {
    func animationDidStop(_ anim: CAAnimation, finished flag: Bool) {
        if flag {
            gradient.colors = gradientSet[currentgradient]
            animateGradient()
        }
    }
}

class aboutUsPage: UIViewController {
    
    @IBOutlet var mainView: UIView!
    let gradient = CAGradientLayer();
    
    var gradientSet = [[CGColor]]();
    var currentgradient: Int = 0;
    
    let one = makeColor(r: 0, g: 200, b: 255).cgColor; // light blue
    let two = makeColor(r: 0, g: 128, b: 255).cgColor; // blue
    let three = makeColor(r: 243, g: 0, b: 255).cgColor; // purple
    let four = makeColor(r: 255, g: 0, b: 89).cgColor; // pink
    let five = makeColor(r: 255, g: 162, b: 0).cgColor; // orange
    let six = makeColor(r: 251, g: 255, b: 0).cgColor; // yellow
    let seven  = makeColor(r: 0, g: 255, b: 19).cgColor; // green
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        let scrollViewFrame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height);
        let scrollView = UIScrollView(frame: scrollViewFrame);
       // scrollView.backgroundColor = UIColor.gray;
        let nameTextHeights = [100, 55, 55, 100, 180];
        let arrayNames = ["Programmers", "Graphic Designers", "Content Editors", "Previous Members", "Founders"];
        let names = ["Kimberly Yu\nEmily Yu\nAlex Dang\nRichard Wei", "Arina Miyadi\nSteffi Huang", "Danielle Wong\nEmily Yu", "Elle Yokata\nMiranda Chen\nJocelyn Thai\nRoselind Zeng", "Seongwook Jang\nJason Zhao\nTiger Ma\nAlbert Yeung\nJessica Chou\nNathan Wong\nPaul Lee\nAlex Hitti"];
 
        let verticalPadding = CGFloat(40);
        let horizontalPadding = CGFloat(45);
        let cornerRadius = CGFloat(5);
        var nextY = CGFloat(50);
        
        let emailViewFrame = CGRect(x: horizontalPadding, y: nextY, width: UIScreen.main.bounds.width - 2*horizontalPadding, height: CGFloat(100));
        let emailView = UIView(frame: emailViewFrame);
        emailView.backgroundColor = UIColor.white;
        
        let emailViewTitle = UILabel();
        emailViewTitle.text = "Contact us at:";
        emailViewTitle.textColor = UIColor.black;
        emailViewTitle.font = UIFont(name: "SFProDisplay-Semibold", size: 20);
        emailViewTitle.sizeToFit();
        emailViewTitle.center = CGPoint(x: emailViewFrame.size.width / 2, y: 25);
        
        let emailClickable = UITextView();
        emailClickable.text = "hsappdev@students.ausd.net";
        emailClickable.textColor = UIColor.black;
        emailClickable.font = UIFont(name: "SFProDisplay-Semibold", size: 15);
        emailClickable.sizeToFit();
        emailClickable.isEditable = false;
        emailClickable.dataDetectorTypes = UIDataDetectorTypes.link;
        emailClickable.center = CGPoint(x: emailViewFrame.size.width / 2, y: 65);
        emailClickable.tintColor = UIColor.systemBlue;
        
        emailView.addSubview(emailClickable);
        emailView.addSubview(emailViewTitle);
        emailView.layer.cornerRadius = cornerRadius;
        scrollView.addSubview(emailView);
        
        nextY += emailViewFrame.size.height + verticalPadding;
        
        
        for i in 0...4{
            let outerView = UIView(frame: CGRect(x: horizontalPadding, y: nextY, width: UIScreen.main.bounds.width - 2*horizontalPadding, height: CGFloat(100))); // temp height
            outerView.backgroundColor = UIColor.white;
            outerView.layer.cornerRadius = cornerRadius;
            var currY = CGFloat(0);
            
            
            let titleLabel = UILabel(frame: CGRect(x: 0, y: 10, width: outerView.frame.size.width, height: 20));
            titleLabel.text = arrayNames[i];
            titleLabel.font = UIFont(name: "SFProText-Bold", size: 18);
            titleLabel.textColor = UIColor.black;
         //   titleLabel.backgroundColor = UIColor.gray;
            titleLabel.textAlignment = .center;
            
            outerView.addSubview(titleLabel);
            currY += 30 + 10;
        
            let nameText = names[i];
            let bodyText = UILabel(frame: CGRect(x: 0, y: currY, width: outerView.frame.size.width, height: CGFloat(nameTextHeights[i])));
            bodyText.text = nameText;
            bodyText.font = UIFont(name: "SFProDisplay-Semibold", size: 16);
            bodyText.textColor = UIColor.gray;
            bodyText.textAlignment = .center;
            bodyText.numberOfLines = 0;
            
            currY += bodyText.frame.size.height + 10;
            
            outerView.addSubview(bodyText);
            outerView.frame.size.height = currY;
            
            scrollView.addSubview(outerView);
            nextY += outerView.frame.size.height + verticalPadding;
        }
        
        scrollView.contentSize = CGSize(width: UIScreen.main.bounds.width, height: nextY);
        
        mainView.insertSubview(scrollView, at: 0);
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        gradientSet.append([one, two])
        gradientSet.append([two, three])
        gradientSet.append([three, four])
        gradientSet.append([four, five]);
        gradientSet.append([five, six]);
        gradientSet.append([six, seven]);
        gradientSet.append([seven, one]);
        
        
        gradient.frame = self.view.bounds
        gradient.colors = gradientSet[currentgradient]
        gradient.startPoint = CGPoint(x:0, y:0)
        gradient.endPoint = CGPoint(x:1, y:1)
        gradient.drawsAsynchronously = true
        mainView.layer.insertSublayer(gradient, at: 0);
        
        animateGradient()
    }
    
    func animateGradient() {
        if currentgradient < gradientSet.count - 1 {
            currentgradient += 1
        } else {
            currentgradient = 0
        }
        
        let gradientChangeAnimation = CABasicAnimation(keyPath: "colors");
        gradientChangeAnimation.duration = 2.0;
        gradientChangeAnimation.toValue = gradientSet[currentgradient];
        gradientChangeAnimation.fillMode = .forwards;
        gradientChangeAnimation.isRemovedOnCompletion = false;
        gradientChangeAnimation.delegate = self;
        gradient.add(gradientChangeAnimation, forKey: "colorChange");
    }
    
    @IBAction func exitCredits(_ sender: Any) {
        dismiss(animated: true);
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
    }
    
    
}
