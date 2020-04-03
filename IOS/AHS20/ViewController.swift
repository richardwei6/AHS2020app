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

extension UIButton{
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
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	
	// TODO: get data from server
    var districtNewsSize = 5;
    var districtNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var sportsNewsSize = 2;
    var sportsNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var asbNewsSize = 3;
    var asbNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	

    
    // func that returns UIcolor from rgb values
    func makeColor(r: Float, g: Float, b: Float) -> UIColor{
        return UIColor.init(red: CGFloat(r/255.0), green: CGFloat(g/255.0), blue: CGFloat(b/255.0), alpha: CGFloat(1.0));
    }
    
    
    override func viewDidLoad() { // setup function
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
		
        
        // District News -----
        for aIndex in 0..<districtNewsSize{
            //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			//districtNewsFrame.size = UIScreen.main.bounds.size;
			districtNewsFrame.origin.x = (districtNewsScrollView.frame.size.width * CGFloat(aIndex));
			districtNewsFrame.size = districtNewsScrollView.frame.size;
			
	  
            
            // create content in scrollview
			let contentView = UIButton(frame: districtNewsFrame); // wrapper for article
			//testButton.setImage(UIImage(named: "ahsldpi.png"), for: .normal);
			contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
			
			// content inside contentView -------
			let articleContentFrame = CGRect(x:0,y:0,width:districtNewsFrame.width,height:districtNewsFrame.height-60);
			let articleContent = UIView(frame: articleContentFrame); // may be image?
			articleContent.backgroundColor = makeColor(r: 138, g: 138, b: 138);
			
			let articleLabelFrame = CGRect(x:0,y:districtNewsFrame.height-60,width:districtNewsFrame.width,height:60);
			let articleLabel = UILabel(frame: articleLabelFrame);
			articleLabel.text = "    " + "Title";
			articleLabel.backgroundColor = UIColor.white;
			articleLabel.font = UIFont(name: "DINCondensed-Bold", size: 30);
			
			
			
			// add content inside contentView to contentview
			contentView.addSubview(articleContent);
			contentView.addSubview(articleLabel);
            // add contentview to scrollview
			contentView.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 30);
            self.districtNewsScrollView.addSubview(contentView);
        }
        // change horizontal size of scrollview
		districtNewsScrollView.contentSize = CGSize(width: (districtNewsScrollView.frame.size.width * CGFloat(districtNewsSize)), height: districtNewsScrollView.frame.size.height);
        districtNewsScrollView.delegate = self;
        
        
        // Sports News -----
		  for aIndex in 0..<sportsNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			  sportsNewsFrame.origin.x = sportsNewsScrollView.frame.size.width * CGFloat(aIndex);
			  sportsNewsFrame.size = sportsNewsScrollView.frame.size;
				  
			
			   // create content in scrollview
			   let contentView = UIButton(frame: sportsNewsFrame); // wrapper for article
			   contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
						 
			   // content inside contentView -------
			   let articleContentFrame = CGRect(x:0,y:0,width:districtNewsFrame.width,height:districtNewsFrame.height-60);
			   let articleContent = UIView(frame: articleContentFrame); // may be image?
			   articleContent.backgroundColor = makeColor(r: 138, g: 138, b: 138);
						 
			   let articleLabelFrame = CGRect(x:0,y:districtNewsFrame.height-60,width:districtNewsFrame.width,height:60);
			   let articleLabel = UILabel(frame: articleLabelFrame);
			   articleLabel.text = "    " + "Title";
	           articleLabel.backgroundColor = UIColor.white;
			   articleLabel.font = UIFont(name: "DINCondensed-Bold", size: 30);
						 
						 
						 
			   // add content inside contentView to contentview
			   contentView.addSubview(articleContent);
			   contentView.addSubview(articleLabel);
			   // add contentview to scrollview
			   contentView.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 30);
			   self.sportsNewsScrollView.addSubview(contentView);
		  }
		  // change horizontal size of scrollview
		  sportsNewsScrollView.contentSize = CGSize(width: (sportsNewsScrollView.frame.size.width * CGFloat(sportsNewsSize)), height: sportsNewsScrollView.frame.size.height);
		  sportsNewsScrollView.delegate = self;
		
		// reset distance
		
		
		// ASB News -----
		  for aIndex in 0..<asbNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			  asbNewsFrame.origin.x = asbNewsScrollView.frame.size.width * CGFloat(aIndex);
			  asbNewsFrame.size = asbNewsScrollView.frame.size;
			
			  
			  // create content in scrollview
			  let contentView = UIButton(frame: asbNewsFrame); // wrapper for article
			  contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
						
			  // content inside contentView -------
			  let articleContentFrame = CGRect(x:0,y:0,width:districtNewsFrame.width,height:districtNewsFrame.height-60);
			  let articleContent = UIView(frame: articleContentFrame); // may be image?
			  articleContent.backgroundColor = makeColor(r: 138, g: 138, b: 138);
						
			  let articleLabelFrame = CGRect(x:0,y:districtNewsFrame.height-60,width:districtNewsFrame.width,height:60);
			  let articleLabel = UILabel(frame: articleLabelFrame);
			  articleLabel.text = "    " + "Title";
			  articleLabel.backgroundColor = UIColor.white;
			  articleLabel.font = UIFont(name: "DINCondensed-Bold", size: 30);
						
						
						
			  // add content inside contentView to contentview
			  contentView.addSubview(articleContent);
			  contentView.addSubview(articleLabel);
			  // add contentview to scrollview
			  contentView.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 30);
			  self.asbNewsScrollView.addSubview(contentView);
		  }
		  // change horizontal size of scrollview
		  asbNewsScrollView.contentSize = CGSize(width: (asbNewsScrollView.frame.size.width * CGFloat(asbNewsSize)) , height: asbNewsScrollView.frame.size.height);
		  asbNewsScrollView.delegate = self;
		
	
        
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews();
		//districtNewsScrollView.setRoundedEdge(corners: [.topRight,.topLeft], radius: 30);
		homeLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight], radius: 30);
	}
	

}

