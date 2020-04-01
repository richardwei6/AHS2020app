//
//  ViewController.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import UIKit

extension UILabel{ // add setRoundedEdge func to UILabel
    func setRoundedEdge(corners:UIRectCorner, radius: CGFloat){ // label.setRoundedEdge([.TopLeft, . TopRight], radius: 10)
        let path = UIBezierPath(roundedRect: bounds, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        layer.mask = mask
    }
}


class ViewController: UIViewController, UIScrollViewDelegate {

    // link UI elements to swift via outlets
    @IBOutlet weak var homeLabel: UILabel!
    @IBOutlet weak var districtNewsScrollView: UIScrollView!
    @IBOutlet weak var districtNewsLabel: UILabel!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsLabel: UILabel!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsLabel: UILabel!
	
	
    
    // 4 is default and 0-3 strings are default
    var districtNewsSize = 10;
    var districtNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var sportsNewsSize = 10;
    var sportsNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var asbNewsSize = 10;
    var asbNewsFrame = CGRect(x:0,y:0,width:0,height:0);

    
    // func that returns UIcolor from rgb values
    
    func makeColor(r: Float, g: Float, b: Float) -> UIColor{
        return UIColor.init(red: CGFloat(r/255.0), green: CGFloat(g/255.0), blue: CGFloat(b/255.0), alpha: CGFloat(1.0));
    }
    
    
    override func viewDidLoad() { // setup function
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        // TODO: fix rounding edges
        
        // District News -----
        for aIndex in 0..<districtNewsSize{
            //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			//districtNewsFrame.size = UIScreen.main.bounds.size;
			districtNewsFrame.origin.x = districtNewsScrollView.frame.size.width * CGFloat(aIndex);
			districtNewsFrame.size = districtNewsScrollView.frame.size;
	  
            
            // create content in scrollview
			let testButton = UIButton(frame: districtNewsFrame);
			testButton.setImage(UIImage(named: "ahsldpi.png"), for: .normal);
			testButton.backgroundColor = makeColor(r: 147, g: 66, b: 78);
            // add contentview to scrollview
            self.districtNewsScrollView.addSubview(testButton);
        }
        // change horizontal size of scrollview
		districtNewsScrollView.contentSize = CGSize(width: districtNewsScrollView.frame.size.width * CGFloat(districtNewsSize), height: districtNewsScrollView.frame.size.height);
        districtNewsScrollView.delegate = self;
        

        
        
        // Sports News -----
		  for aIndex in 0..<sportsNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			  sportsNewsFrame.origin.x = sportsNewsScrollView.frame.size.width * CGFloat(aIndex);
			  sportsNewsFrame.size = sportsNewsScrollView.frame.size;
		
			  
			  // create content in scrollview
			  let testButton = UIButton(frame: sportsNewsFrame);
			  testButton.setImage(UIImage(named: "ahsldpi.png"), for: .normal);
			  testButton.backgroundColor = makeColor(r: 147, g: 66, b: 78);
			  // add contentview to scrollview
			  self.sportsNewsScrollView.addSubview(testButton);
		  }
		  // change horizontal size of scrollview
		  sportsNewsScrollView.contentSize = CGSize(width: sportsNewsScrollView.frame.size.width * CGFloat(sportsNewsSize), height: sportsNewsScrollView.frame.size.height);
		  sportsNewsScrollView.delegate = self;
		
		// ASB News -----
		  for aIndex in 0..<asbNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			  asbNewsFrame.origin.x = sportsNewsScrollView.frame.size.width * CGFloat(aIndex);
			  asbNewsFrame.size = asbNewsScrollView.frame.size;
		
			  
			  // create content in scrollview
			  let testButton = UIButton(frame: asbNewsFrame);
			  testButton.setImage(UIImage(named: "ahsldpi.png"), for: .normal);
			  testButton.backgroundColor = makeColor(r: 147, g: 66, b: 78);
			  // add contentview to scrollview
			  self.asbNewsScrollView.addSubview(testButton);
		  }
		  // change horizontal size of scrollview
		  asbNewsScrollView.contentSize = CGSize(width: asbNewsScrollView.frame.size.width * CGFloat(asbNewsSize), height: asbNewsScrollView.frame.size.height);
		  asbNewsScrollView.delegate = self;
        
        
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews();
		//districtNewsScrollView.setRoundedEdge(corners: [.topRight,.topLeft], radius: 30);
		districtNewsLabel.setRoundedEdge(corners: [.bottomRight,.bottomLeft], radius: 30);
		sportsNewsLabel.setRoundedEdge(corners:[.bottomRight,.bottomLeft], radius: 30);
		asbNewsLabel.setRoundedEdge(corners:[.bottomRight,.bottomLeft], radius: 30);
		homeLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight], radius: 30);
	}
	

}

