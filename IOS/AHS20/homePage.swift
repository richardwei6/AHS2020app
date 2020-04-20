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


class homeClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    // link UI elements to swift via outlets
	
	//@IBOutlet weak var tabBar: UITabBar!
	@IBOutlet weak var monthLabel: UILabel!
	@IBOutlet weak var featuredScrollView: UIScrollView!
	@IBOutlet weak var featuredPageControl: UIPageControl!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsPageControl: UIPageControl!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsPageControl: UIPageControl!
	@IBOutlet weak var districtNewsScrollView: UIScrollView!
	@IBOutlet weak var districtNewsPageControl: UIPageControl!
	
	
	// TODO: get data from server
	var featuredSize = 10;
	var featuredFrame = CGRect(x:0,y:0,width:0,height:0);
	var asbNewsSize = 3;
    var asbNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var sportsNewsSize = 2;
    var sportsNewsFrame = CGRect(x:0,y:0,width:0,height:0);
    var districtNewsSize = 5;
    var districtNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	
	
	
	// func that returns UIcolor from rgb values
	   func makeColor(r: Float, g: Float, b: Float) -> UIColor{
		   return UIColor.init(red: CGFloat(r/255.0), green: CGFloat(g/255.0), blue: CGFloat(b/255.0), alpha: CGFloat(1.0));
	   }
	
	


    
    override func viewDidLoad() { // setup function
        super.viewDidLoad()
		
        // Do any additional setup after loading the view.
		
		
		// article variables
		let articleHorizontalPadding = CGFloat(10);
		let articleVerticalPadding = CGFloat(5);
		
		
        //let articleGreyBackground = makeColor(r: 239, g: 247, b: 237);
		let articleDarkGreyBackground = makeColor(r: 143, g: 142, b: 142);
		// scrollview variables
		let scrollViewHorizontalConstraints = CGFloat(50);
		
		
		// Featured News -----
		featuredPageControl.numberOfPages = featuredSize;
		featuredFrame.size = featuredScrollView.frame.size;
		featuredFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
        for aIndex in 0..<featuredSize{
			featuredFrame.origin.x = (featuredFrame.size.width * CGFloat(aIndex));
			
            // create content in scrollview
			let contentView = UIButton(frame: featuredFrame); // wrapper for article
			//contentView.backgroundColor = articleGreyBackground;
			
			
			let articleImageViewFrame = CGRect(x: articleHorizontalPadding, y: articleVerticalPadding, width: featuredFrame.size.width-(2*articleHorizontalPadding), height: featuredFrame.size.height-60-articleVerticalPadding);
			let articleImageView = UIImageView(frame:articleImageViewFrame);
			articleImageView.backgroundColor = articleDarkGreyBackground;
			articleImageView.layer.cornerRadius = 10;
			
			// time stamp
			let articleTimestampFrame = CGRect(x: articleImageViewFrame.size.width - 60, y: articleImageViewFrame.size.height - 30, width: 50, height: 20);
			let articleTimestamp = UILabel(frame: articleTimestampFrame);
			articleTimestamp.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			articleTimestamp.font = UIFont(name: "SFProDisplay-Regular", size: 10);
			articleTimestamp.textAlignment = .center;
			articleTimestamp.textColor = makeColor(r: 57, g: 57, b: 57);
			articleTimestamp.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			articleTimestamp.text = "1 hour ago"; // insert code here to get time of article

			
			//bookmark image button - 30x30
			let bookmarkFrame = CGRect(x: articleImageViewFrame.size.width - 40, y: 10, width: 30, height: 30);
			let bookmarkButton = UIButton(frame: bookmarkFrame);
			bookmarkButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			let bookmarkImage = UIImage(systemName: "bookmark"); // get system image
			bookmarkButton.setImage(bookmarkImage, for: .normal);
			
			
			
			articleImageView.addSubview(bookmarkButton); // add bookmark button to imageview
			articleImageView.addSubview(articleTimestamp); // add timestamp to imageview
			
			
			let articleTitleFrame = CGRect(x: articleHorizontalPadding+1, y: featuredFrame.size.height - 60, width: featuredFrame.size.width-(2*articleHorizontalPadding)-2, height: 60);
			let articleTitleLabel = UILabel(frame: articleTitleFrame);
			articleTitleLabel.text = "Lorem Ipsum Long Title";
			articleTitleLabel.textAlignment = .left;
			articleTitleLabel.font = UIFont(name:"SFProText-Bold",size: 25);
			
			
			
            // add contentview to scrollview
			contentView.addSubview(articleTitleLabel);
			contentView.addSubview(articleImageView);
			
			
            self.featuredScrollView.addSubview(contentView);
        }
        // change horizontal size of scrollview
		featuredScrollView.contentSize = CGSize(width: (featuredFrame.size.width * CGFloat(featuredSize)), height: featuredScrollView.frame.size.height);
        featuredScrollView.delegate = self;
		
		
		// ASB News -----
		asbNewsPageControl.numberOfPages = asbNewsSize;
		asbNewsFrame.size = asbNewsScrollView.frame.size;
		asbNewsFrame.size.width = UIScreen.main.bounds.width - scrollViewHorizontalConstraints;
		  for aIndex in 0..<asbNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			asbNewsFrame.origin.x = asbNewsFrame.size.width * CGFloat(aIndex);
			
			  
			  // create content in scrollview
			  let contentView = UIButton(frame: asbNewsFrame); // wrapper for article
			  contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
						
			  /*// content inside contentView -------
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
			  // add contentview to scrollview*/
			  contentView.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 30);
			  self.asbNewsScrollView.addSubview(contentView);
		  }
		  // change horizontal size of scrollview
		  asbNewsScrollView.contentSize = CGSize(width: (asbNewsFrame.size.width * CGFloat(asbNewsSize)) , height: asbNewsScrollView.frame.size.height);
		  asbNewsScrollView.delegate = self;
		
        
		
		
        // Sports News -----
		sportsNewsPageControl.numberOfPages = sportsNewsSize;
		sportsNewsFrame.size = sportsNewsScrollView.frame.size;
		sportsNewsFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
		  for aIndex in 0..<sportsNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			sportsNewsFrame.origin.x = sportsNewsFrame.size.width * CGFloat(aIndex);
				  
			
			   // create content in scrollview
			   let contentView = UIButton(frame: sportsNewsFrame); // wrapper for article
			   contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
						 
			   // content inside contentView -------
			  /* let articleContentFrame = CGRect(x:0,y:0,width:districtNewsFrame.width,height:districtNewsFrame.height-60);
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
			   // add contentview to scrollview*/
			   contentView.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 30);
			   self.sportsNewsScrollView.addSubview(contentView);
		  }
		  // change horizontal size of scrollview
		sportsNewsScrollView.contentSize = CGSize(width: (sportsNewsFrame.size.width * CGFloat(sportsNewsSize)), height: sportsNewsScrollView.frame.size.height);
		  sportsNewsScrollView.delegate = self;
		
		
		// District News -----
		districtNewsPageControl.numberOfPages = districtNewsSize;
		districtNewsFrame.size = districtNewsScrollView.frame.size;
		districtNewsFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
        for aIndex in 0..<districtNewsSize{
            //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			//districtNewsFrame.size = UIScreen.main.bounds.size;
			districtNewsFrame.origin.x = (districtNewsFrame.size.width * CGFloat(aIndex));
			
            // create content in scrollview
			let contentView = UIButton(frame: districtNewsFrame); // wrapper for article
			//testButton.setImage(UIImage(named: "ahsldpi.png"), for: .normal);
			contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
			
			/*// content inside contentView -------
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
            // add contentview to scrollview*/
			contentView.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 30);
            self.districtNewsScrollView.addSubview(contentView);
        }
        // change horizontal size of scrollview
		districtNewsScrollView.contentSize = CGSize(width: (districtNewsFrame.size.width * CGFloat(districtNewsSize)), height: districtNewsScrollView.frame.size.height);
        districtNewsScrollView.delegate = self;
		
		
		
		
		
	  
        
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews();
		//homeLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight], radius: 30);
	}
	
	
	func  scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
		featuredPageControl.currentPage = Int(featuredScrollView.contentOffset.x / featuredFrame.size.width);
		
		asbNewsPageControl.currentPage = Int(asbNewsScrollView.contentOffset.x / asbNewsFrame.size.width);
		
		sportsNewsPageControl.currentPage = Int(sportsNewsScrollView.contentOffset.x / sportsNewsFrame.size.width);
		
		districtNewsPageControl.currentPage = Int(districtNewsScrollView.contentOffset.x / districtNewsFrame.size.width);
	}
	

}

