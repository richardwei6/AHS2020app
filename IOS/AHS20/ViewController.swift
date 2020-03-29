//
//  ViewController.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import UIKit


extension UILabel{
    func setRoundedEdge(corners:UIRectCorner, radius: CGFloat){ // label.setRoundedEdge([.TopLeft, . TopRight], radius: 10)
        let maskPath1 = UIBezierPath(roundedRect: bounds,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius))
        let maskLayer1 = CAShapeLayer()
        maskLayer1.frame = bounds
        maskLayer1.path = maskPath1.cgPath
        layer.mask = maskLayer1
    }
}


class ViewController: UIViewController,UIScrollViewDelegate {

    // link UI elements to swift via outlets
    @IBOutlet weak var homeLabel: UILabel!
    
    
    
    // 4 is default and 0-3 strings are default
    //var ausdNewsArr: [String] = ["0","1","2","3"]
    /*var ausdNewsSize = 10
    var ausdNewsframe = CGRect(x:0,y:0,width:0,height:0)
    
    var clubSize = 4
    var clubframe = CGRect(x:0,y:0,width:0,height:0)
    
    var asbSize = 4
    var asbframe = CGRect(x:0,y:0,width:0,height:0)
    
    var otherSize = 4
    var otherframe = CGRect(x:0,y:0,width:0,height:0)
    
    var sportsSize = 4
    var sportsframe = CGRect(x:0,y:0,width:0,height:0)*/
    
    // func that returns UIcolor from rgb values
    
    func makeColor(r: Float, g: Float, b: Float) -> UIColor{
        return UIColor.init(red: CGFloat(r/255.0), green: CGFloat(g/255.0), blue: CGFloat(b/255.0), alpha: CGFloat(1.0));
    }
    
    
    override func viewDidLoad() { // setup function
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        homeLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight], radius: 30);
        
        // TODO: fix textviews almost overlapping the title on scrollviews - also fix scrollview and constraint issues on smaller screens like iPhone SE.
        
        // ausdnews - 4 is default
        /*ausdNewsPageControl.numberOfPages = ausdNewsSize;
        ausdNewsScrollView.layer.cornerRadius = 20;
        for articleIndex in 0..<ausdNewsSize {
            // set up frame
            ausdNewsframe.origin.x = ausdNewsScrollView.frame.size.width * CGFloat(articleIndex);
            ausdNewsframe.size = ausdNewsScrollView.frame.size;
            // set up text onto scrollview
            // TODO: implement array that has needed text from firebase
            let articleBackground = makeColor(r: 147, g: 66, b: 78);
            let textColor = UIColor.white;
            
            let contentView = UIView(frame: ausdNewsframe); // content view will contain text and image
            contentView.backgroundColor = articleBackground;
            contentView.layer.cornerRadius = 20;
            
            let titletextFrame = CGRect(x: 95.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text; x is from 0 to 400
            let titletextView = UITextView(frame: titletextFrame);
            titletextView.text = "Title here";
            titletextView.textAlignment = .center;
            titletextView.backgroundColor = articleBackground;
            titletextView.textColor = textColor;
            titletextView.isEditable = false;
            //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
            
            let imageFrame = CGRect(x: 40.0, y: 60.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
            // image of article
            
            let articletextFrame = CGRect(x: 170.0, y: 20.0, width: 200.0, height: 200.0);
            let articletextView = UITextView(frame: articletextFrame);
            articletextView.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            articletextView.textAlignment = .left;
            articletextView.backgroundColor = articleBackground;
            articletextView.textColor = textColor;
            articletextView.isEditable = false;
            
            // add contentView to scrollview
            contentView.addSubview(titletextView);
            contentView.addSubview(imageview);
            contentView.addSubview(articletextView);
            self.ausdNewsScrollView.addSubview(contentView);
        }
        // set size of scrollview
        ausdNewsScrollView.contentSize = CGSize(width: (ausdNewsScrollView.frame.size.width * CGFloat(ausdNewsSize)), height: ausdNewsScrollView.frame.size.height);
        ausdNewsScrollView.delegate = self;
        
        
        
        
        
        // clubs
        for pageIndex in 0..<clubSize {
            // set up frame
            clubframe.origin.x = clubScrollView.frame.size.width * CGFloat(pageIndex);
            clubframe.size = clubScrollView.frame.size;
            
            // content goes here:
            // TODO: implement array that has needed text from firebase
            let articleBackground = makeColor(r: 220, g: 220, b: 220);
            let textColor = UIColor.black;
            
            let contentView = UIView(frame: clubframe); // content view will contain text and image
            contentView.backgroundColor = articleBackground;
            contentView.layer.cornerRadius = 20;
                       
            let titletextFrame = CGRect(x: 95.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text; x is from 0 to 400
            let titletextView = UITextView(frame: titletextFrame);
            titletextView.text = "Title here";
            titletextView.textAlignment = .center;
            titletextView.backgroundColor = articleBackground;
            titletextView.textColor = textColor;
            titletextView.isEditable = false;
                       //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
                       
            let imageFrame = CGRect(x: 40.0, y: 90.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
                       // image of article
                       
            let articletextFrame = CGRect(x: 170.0, y: 20.0, width: 200.0, height: 260.0);
            let articletextView = UITextView(frame: articletextFrame);
            articletextView.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            articletextView.textAlignment = .left;
            articletextView.backgroundColor = articleBackground;
            articletextView.textColor = textColor;
            articletextView.isEditable = false;
                       
            // add contentView to scrollview
            contentView.addSubview(titletextView);
            contentView.addSubview(imageview);
            contentView.addSubview(articletextView);
            
            self.clubScrollView.addSubview(contentView);
        }
        // set size of scrollview
        clubScrollView.contentSize = CGSize(width: (clubScrollView.frame.size.width * CGFloat(clubSize)), height: clubScrollView.frame.size.height);
        clubScrollView.delegate = self;
        
        
        
        
        //asb
        for pageIndex in 0..<asbSize {
            // set up frame
            asbframe.origin.x = asbScrollView.frame.size.width * CGFloat(pageIndex);
            asbframe.size = asbScrollView.frame.size;
            
            // content goes here:
            // TODO: implement array that has needed text from firebase
            let articleBackground = makeColor(r: 220, g: 220, b: 220);
            let textColor = UIColor.black;
            
            let contentView = UIView(frame: asbframe); // content view will contain text and image
            contentView.backgroundColor = articleBackground;
            contentView.layer.cornerRadius = 20;
                       
            let titletextFrame = CGRect(x: 95.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text; x is from 0 to 400
            let titletextView = UITextView(frame: titletextFrame);
            titletextView.text = "Title here";
            titletextView.textAlignment = .center;
            titletextView.backgroundColor = articleBackground;
            titletextView.textColor = textColor;
            titletextView.isEditable = false;
                       //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
                       
            let imageFrame = CGRect(x: 40.0, y: 90.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
                       // image of article
                       
            let articletextFrame = CGRect(x: 170.0, y: 20.0, width: 200.0, height: 260.0);
            let articletextView = UITextView(frame: articletextFrame);
            articletextView.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            articletextView.textAlignment = .left;
            articletextView.backgroundColor = articleBackground;
            articletextView.textColor = textColor;
            articletextView.isEditable = false;
                       
            // add contentView to scrollview
            contentView.addSubview(titletextView);
            contentView.addSubview(imageview);
            contentView.addSubview(articletextView);
            
            
            self.asbScrollView.addSubview(contentView);
        }
        // set size of scrollview
        asbScrollView.contentSize = CGSize(width: (asbScrollView.frame.size.width * CGFloat(asbSize)), height: asbScrollView.frame.size.height);
        asbScrollView.delegate = self;
        
        
        
        
        
        //other
        for pageIndex in 0..<otherSize {
            // set up frame
            otherframe.origin.x = otherScrollView.frame.size.width * CGFloat(pageIndex);
            otherframe.size = otherScrollView.frame.size;
            
            let articleBackground = makeColor(r: 220, g: 220, b: 220);
            let textColor = UIColor.black;
            
            let contentView = UIView(frame: otherframe); // content view will contain text and image
            contentView.backgroundColor = articleBackground;
            contentView.layer.cornerRadius = 20;
                       
            let titletextFrame = CGRect(x: 95.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text; x is from 0 to 400
            let titletextView = UITextView(frame: titletextFrame);
            titletextView.text = "Title here";
            titletextView.textAlignment = .center;
            titletextView.backgroundColor = articleBackground;
            titletextView.textColor = textColor;
            titletextView.isEditable = false;
                       //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
                       
            let imageFrame = CGRect(x: 40.0, y: 90.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
                       // image of article
                       
            let articletextFrame = CGRect(x: 170.0, y: 20.0, width: 200.0, height: 260.0);
            let articletextView = UITextView(frame: articletextFrame);
            articletextView.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            articletextView.textAlignment = .left;
            articletextView.backgroundColor = articleBackground;
            articletextView.textColor = textColor;
            articletextView.isEditable = false;
                       
            // add contentView to scrollview
            contentView.addSubview(titletextView);
            contentView.addSubview(imageview);
            contentView.addSubview(articletextView);
            
            self.otherScrollView.addSubview(contentView);
        }
        // set size of scrollview
        otherScrollView.contentSize = CGSize(width: (otherScrollView.frame.size.width * CGFloat(otherSize)), height: otherScrollView.frame.size.height);
        otherScrollView.delegate = self;
        
        
        
        //sports
        sportsPageControl.numberOfPages = sportsSize;
        sportsScrollView.layer.cornerRadius = 20;
        for articleIndex in 0..<sportsSize {
            // set up frame
            sportsframe.origin.x = sportsScrollView.frame.size.width * CGFloat(articleIndex);
            sportsframe.size = sportsScrollView.frame.size;
            // set up text onto scrollview
            // TODO: implement array that has needed text from firebase
            let articleBackground = makeColor(r: 147, g: 66, b: 78);
            let textColor = UIColor.white;
            
            let contentView = UIView(frame: sportsframe); // content view will contain text and image
            contentView.backgroundColor = articleBackground;
            contentView.layer.cornerRadius = 20;
                       
            let titletextFrame = CGRect(x: 95.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text; x is from 0 to 400
            let titletextView = UITextView(frame: titletextFrame);
            titletextView.text = "Title here";
            titletextView.textAlignment = .center;
            titletextView.backgroundColor = articleBackground;
            titletextView.textColor = textColor;
            titletextView.isEditable = false;
                       //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
                       
            let imageFrame = CGRect(x: 40.0, y: 90.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
                       // image of article
                       
            let articletextFrame = CGRect(x: 170.0, y: 20.0, width: 200.0, height: 260.0);
            let articletextView = UITextView(frame: articletextFrame);
            articletextView.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            articletextView.textAlignment = .left;
            articletextView.backgroundColor = articleBackground;
            articletextView.textColor = textColor;
            articletextView.isEditable = false;
                       
            // add contentView to scrollview
            contentView.addSubview(titletextView);
            contentView.addSubview(imageview);
            contentView.addSubview(articletextView);
            self.sportsScrollView.addSubview(contentView);
        }
        // set size of scrollview
        sportsScrollView.contentSize = CGSize(width: (sportsScrollView.frame.size.width * CGFloat(sportsSize)), height: sportsScrollView.frame.size.height);
        sportsScrollView.delegate = self;
        
        
        // end setup for scrollviews
        // setup Upcoming views
        Upcoming1Text.font = .systemFont(ofSize: 12);
        Upcoming2Text.font = .systemFont(ofSize: 12);
        Upcoming3Text.font = .systemFont(ofSize: 12);
        Upcoming4Text.font = .systemFont(ofSize: 12);*/
    }
    // every time scrollview stops moving
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        // gets current page num
        // set dot of pagecontrol to correct page
        
        //ausdNewsPageControl.currentPage = Int(ausdNewsScrollView.contentOffset.x / ausdNewsScrollView.frame.size.width); // Typecasting float to int
        
        //sportsPageControl.currentPage = Int(sportsScrollView.contentOffset.x / sportsScrollView.frame.size.width); // Typecasting float to int
        
    }

}

