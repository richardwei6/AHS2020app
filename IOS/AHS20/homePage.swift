//
//  ViewController.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//


// ----- READ: Hello whomever might be reading this. I have many custom features added to this code that you won't find on stock swift projects. This is why I have included some notes that you might want to read below:
/*
- sharedFunc.swift includes the shared functions/classes between all swift files. You can access any of theses functions from any swift file
- CustomUIButton is a custom class that I created that is an extension of the regular UIButton class. The main different to this class is that there are extra data types that allow you allow you to pass data to a ".addTarget" @objc function that you normally wouldn't be able to do. The data types can be found in sharedFunc.swift
*/
import UIKit
import AudioToolbox
import Firebase
import FirebaseDatabase

class homeClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {
	
	// link UI elements to swift via outlets
	
	//@IBOutlet weak var tabBar: UITabBar!
	/*@IBOutlet weak var featuredScrollView: UIScrollView!
	@IBOutlet weak var featuredPageControl: UIPageControl!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsPageControl: UIPageControl!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsPageControl: UIPageControl!
	@IBOutlet weak var districtNewsScrollView: UIScrollView!
	@IBOutlet weak var districtNewsPageControl: UIPageControl!*/
	@IBOutlet weak var mainScrollView: UIScrollView!
	@IBOutlet weak var featuredScrollView: UIScrollView!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsPageControl: UIPageControl!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsPageControl: UIPageControl!
	@IBOutlet weak var districtNewsScrollView: UIScrollView!
	@IBOutlet weak var districtNewsPageControl: UIPageControl!
	
	@IBOutlet weak var loadingASBView: UIView!
	@IBOutlet weak var loadingDistrictView: UIView!
	@IBOutlet weak var loadingSportsView: UIView!
	
	@IBOutlet weak var featuredLabel: UILabel!
	@IBOutlet weak var asbLabel: UILabel!
	@IBOutlet weak var sportsLabel: UILabel!
	@IBOutlet weak var districtLabel: UILabel!
	
	
	
	@IBOutlet weak var featuredMissingLabel: UILabel!
	
	let loading = "Loading...";
	
	let bookmarkImageVerticalInset = CGFloat(5);
	let bookmarkImageHorizontalInset = CGFloat(7);
	
	let bookmarkImageUI = UIImage(named: "invertedbookmark");
	//let bookmarkImageUI = UIImage(systemName: "bookmark");
	
	// TODO: get data from server
	var featuredSize = 6;
	var featuredFrame = CGRect(x:0,y:0,width:0,height:0);
	var asbNewsSize = 1;
	var asbNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var sportsNewsSize = 1;
	var sportsNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var districtNewsSize = 1;
	var districtNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	
	var refreshControl = UIRefreshControl();
	var featuredArticles = [articleData]();
	
	func getHomeArticleData(){
		setUpConnection();
		if (internetConnected){
			print("ok -------- loading articles - home");
			featuredArticles = [articleData]();
			homeArticleList = [[articleData]](repeating: [articleData](), count: 3);
			
			for i in 0...2{
				var s: String; // path inside homepage
				switch i {
				case 0: // asb
					s = "asb";
					break;
				case 1: // sports
					s = "sports";
					break;
				case 2: // district
					s = "district";
					break;
				default:
					s = "";
					break;
				}
				
				//print(s);
				ref.child("homepage").child(s).observeSingleEvent(of: .value) { (snapshot) in
					//print(s);
					
					//print(snapshot.childrenCount)
					let enumerator = snapshot.children;
					var temp = [articleData](); // temporary array
					while let article = enumerator.nextObject() as? DataSnapshot{ // each article
						//print(article);
						
						
						let enumerator = article.children;
						var singleArticle = articleData();
						
						singleArticle.articleID = article.key as! String;
						
						
						while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
						
							
							if (articleContent.key == "articleAuthor"){
								singleArticle.articleAuthor = articleContent.value as? String;
							}
							else if (articleContent.key == "articleBody"){
								singleArticle.articleBody = articleContent.value as? String;
							}
							else if (articleContent.key == "articleUnixEpoch"){
								singleArticle.articleUnixEpoch = articleContent.value as? Int64;
							}
							else if (articleContent.key == "articleImages"){
								
								var tempImage = [String]();
								let imageIt = articleContent.children;
								while let image = imageIt.nextObject() as? DataSnapshot{
									tempImage.append(image.value as! String);
								}
								//print(tempImage)
								singleArticle.articleImages = tempImage;
							}
							else if (articleContent.key == "articleTitle"){
								
								singleArticle.articleTitle = articleContent.value as? String;
							}
							else if (articleContent.key == "isFeatured"){
								singleArticle.isFeatured = (articleContent.value as? Int == 0 ? false : true);
							}
							else if (articleContent.key == "hasHTML"){
								singleArticle.hasHTML = (articleContent.value as? Int == 0 ? false : true);
							}
							
							
						}
						// print("append to main")
						// print(temp.count);
						//print(singleArticle.articleTitle);
						//print(singleArticle.articleImages);
						singleArticle.articleCatagory = s.prefix(1).capitalized + s.dropFirst();
						if (singleArticle.articleCatagory == "Asb"){
							singleArticle.articleCatagory = "ASB";
						}
						temp.append(singleArticle);
						//print(singleArticle.isFeatured);
						if (singleArticle.isFeatured == true){
							self.featuredArticles.append(singleArticle);
						}
					}
					self.loadingASBView.isHidden = true;
					self.loadingDistrictView.isHidden = true;
					self.loadingSportsView.isHidden = true;
					homeArticleList[i] = temp;
					self.setUpAllViews();
					self.refreshControl.endRefreshing();
				};
			}
		}
		else{
			//setUpAllViews();
			print("no network detected - home");
			featuredLabel.text = "NO Connection";
			asbLabel.text = "NO Connection";
			sportsLabel.text = "NO Connection";
			districtLabel.text = "NO Connection";
			let infoPopup = UIAlertController(title: "No internet connection detected", message: "No articles were loaded", preferredStyle: UIAlertController.Style.alert);
			infoPopup.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
				self.refreshControl.endRefreshing();
			}));
			present(infoPopup, animated: true, completion: nil);
		}
	}
	
	func setUpColorOfBookmark(sender: inout CustomUIButton){
		if (savedArticleClass.isSavedCurrentArticle(articleID: sender.articleCompleteData.articleID!)){ // TODO: implement sender.articleID
			sender.tintColor = mainThemeColor;
		}
		else{
			sender.tintColor = UIColor.white;
		}
	}
	
	
	@objc func openArticle(sender: CustomUIButton){
		print("Button pressed");
		let articleDataDict: [String: articleData] = ["articleContent" : sender.articleCompleteData];
		NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
	}

	
	@objc func bookmarkCurrentArticle(sender: CustomUIButton){
		//print("bookmark - \(savedArticleClass.isSavedCurrentArticle(articleID: sender.articleCompleteData.articleID!))")
		//print("Bookmark Button - \(sender.articleCompleteData)");
		if (savedArticleClass.isSavedCurrentArticle(articleID: sender.articleCompleteData.articleID!) == false){
			sender.tintColor = mainThemeColor;
			savedArticleClass.saveCurrArticle(articleID: sender.articleCompleteData.articleID!, article: sender.articleCompleteData);
		}
		else{
			sender.tintColor = UIColor.white;
			savedArticleClass.removeCurrArticle(articleID: sender.articleCompleteData.articleID!);
		}
		if (sender.articleCompleteData.isFeatured){
			setUpAllViews();
		}
	}
	
	
	func smallArticle(x: CGFloat, y: CGFloat, width: CGFloat, height: CGFloat, articleSingle: articleData) -> CustomUIButton{//TODO: find out a way to separate article from top and bottom
		
		let mainArticleFrame = CGRect(x: x, y: y, width: width, height: height);
		let mainArticleView = CustomUIButton(frame: mainArticleFrame);
		
		
		let articleTextWidth = (width/2) + 10;
		
		
		let articleImageViewFrame = CGRect(x: 0, y: 5, width: width - articleTextWidth, height: height - 10);
		let articleImageView = UIImageView(frame: articleImageViewFrame);
		if (articleSingle.articleImages?.count ?? 0 >= 1){
			articleImageView.imgFromURL(sURL: articleSingle.articleImages?[0] ?? "");
			articleImageView.contentMode = .scaleAspectFill;
		}
		articleImageView.backgroundColor = UIColor.gray;
	    articleImageView.setRoundedEdge(corners: [.topLeft, .topRight, .bottomLeft, .bottomRight], radius: 10);
		
		let spacing = CGFloat(10);
		
		let articleTitleFrame = CGRect(x: articleImageViewFrame.size.width + spacing, y: 0, width: articleTextWidth-spacing, height: min(articleSingle.articleTitle?.getHeight(withConstrainedWidth: articleTextWidth-spacing, font: UIFont(name: "SFProDisplay-Semibold", size: 18)!) ?? 50, 50));
		let articleTitle = UILabel(frame: articleTitleFrame);
		articleTitle.text = articleSingle.articleTitle ?? "";
		articleTitle.textAlignment = .left;
		articleTitle.font = UIFont(name: "SFProDisplay-Semibold", size: 18);
	//	articleTitle.lineBreakMode = .byWordWrapping;
		articleTitle.numberOfLines = 0;
		
		var text = "";
		if (articleSingle.hasHTML){
			text = parseHTML(s: articleSingle.articleBody ?? "").string;
		}
		else{
			text = (articleSingle.articleBody ?? "");
		}
		let articleBodyFrame = CGRect(x: articleImageViewFrame.size.width + spacing, y: articleTitleFrame.maxY, width: articleTextWidth-spacing, height: min(mainArticleView.frame.height - articleTitleFrame.maxY, text.getHeight(withConstrainedWidth: articleTextWidth-spacing, font: UIFont(name: "SFProDisplay-Regular", size: 14)!)));
		let articleBody = UILabel(frame: articleBodyFrame);
		articleBody.text = text;
		articleBody.textAlignment = .left;
		articleBody.font = UIFont(name: "SFProDisplay-Regular", size: 14);
		articleBody.numberOfLines = 0;
		//articleBody.backgroundColor = UIColor.gray;
		
		/*let articleBodyFrame = CGRect(x: articleImageViewFrame.size.width + spacing, y: articleTitleFrame.maxY, width: articleTextWidth-spacing, height: mainArticleView.frame.height - articleTitleFrame.size.height);
		let articleBody = UILabel(frame: articleBodyFrame);
		if (articleSingle.hasHTML){
			articleBody.text = parseHTML(s: articleSingle.articleBody ?? "").string;
		}
		else{
			articleBody.text = (articleSingle.articleBody ?? "");
		}
		articleBody.textAlignment = .left;
		articleBody.font = UIFont(name: "SFProDisplay-Regular", size: 14);
		articleBody.numberOfLines = 0;*/
		
		let timeStampText = epochClass.epochToString(epoch: articleSingle.articleUnixEpoch ?? -1);
		let timeStampFrame = CGRect(x: 7, y: height - 25, width: timeStampText.getWidth(withConstrainedHeight: 15, font: UIFont(name: "SFProDisplay-Semibold", size: 8)!) + 10, height: 15);
		let timeStamp = UILabel(frame: timeStampFrame);
		timeStamp.text = timeStampText;
		//timeStamp.text = "12 months ago";
		timeStamp.textAlignment = .center;
		timeStamp.textColor = UIColor.gray;
		timeStamp.font = UIFont(name: "SFProDisplay-Semibold", size: 8);
		timeStamp.backgroundColor = UIColor.white;
		timeStamp.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 3);
		
		
		
		mainArticleView.addSubview(articleImageView);
		mainArticleView.addSubview(articleTitle);
		mainArticleView.addSubview(articleBody);
		mainArticleView.addSubview(timeStamp);
		
		//articleImageView.layer.cornerRadius = 10;
		mainArticleView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
		
		//mainArticleView.backgroundColor = UIColor.lightGray;
		mainArticleView.articleCompleteData = articleSingle;
		
		return mainArticleView;
	}
	
	func arrayToPairs(a: [articleData]) -> [[articleData]]{
		var ans = [[articleData]]();
		var temp = [articleData](); // pairs
		for i in a{
			temp.append(i);
			if (temp.count == 2){
				ans.append(temp);
				temp = [articleData]();
			}
		}
		if (temp.count > 0){
			ans.append(temp);
		}
		return ans;
	}
	
	func setUpAllViews(){
		
		//	print("home");
		//	print(homeArticleList.count);
		setUpConnection();
		if (internetConnected && (homeArticleList[0].count > 0 || homeArticleList[1].count > 0 || homeArticleList[2].count > 0)){
			
			featuredLabel.text = "Featured";
			asbLabel.text = "ASB News";
			sportsLabel.text = "Sports News";
			districtLabel.text = "District News";
			
			//print("home");
			//print(homeArticleList)
			let asbArticlePairs = arrayToPairs(a: homeArticleList[0]);
			let sportsArticlePairs = arrayToPairs(a: homeArticleList[1]);
			let districtArticlePairs = arrayToPairs(a: homeArticleList[2]);
			featuredSize = featuredArticles.count;
			asbNewsSize = asbArticlePairs.count;
			sportsNewsSize = sportsArticlePairs.count;
			districtNewsSize = districtArticlePairs.count;
			/*print(asbArticlePairs);
			print(sportsArticlePairs);
			print(districtArticlePairs);
			print("home")
			print(featuredSize);
			print(asbNewsSize);
			print(sportsNewsSize);
			print(districtNewsSize);*/
			
			//let bookMarkBackground = makeColor(r: 165, g: 165, b: 165);
			
			
			//let articleDarkGreyBackground = makeColor(r: 143, g: 142, b: 142);
			// scrollview variables
			let scrollViewHorizontalConstraints = CGFloat(38);
			
			for view in featuredScrollView.subviews{
				if (view.tag == 1){
					view.removeFromSuperview();
				}
			}
			for view in asbNewsScrollView.subviews{
				view.removeFromSuperview();
			}
			for view in sportsNewsScrollView.subviews{
				view.removeFromSuperview();
			}
			for view in districtNewsScrollView.subviews{
				view.removeFromSuperview();
			}
			
			if (featuredSize > 0){
				// Featured News ----- NOTE - article is not created by smallArticle() func
				featuredScrollView.flashScrollIndicators();
				featuredMissingLabel.isHidden = true;
				featuredScrollView.isHidden = false;
				featuredFrame.size = featuredScrollView.frame.size;
				featuredFrame.size.height -= 15;
				featuredFrame.size.width = UIScreen.main.bounds.size.width;
				featuredScrollView.contentSize = CGSize(width: (featuredFrame.size.width * CGFloat(featuredSize)), height: featuredScrollView.frame.size.height);
				for aIndex in 0..<featuredSize{
					featuredFrame.origin.x = (featuredFrame.size.width * CGFloat(aIndex));
					
					let currArticle = featuredArticles[aIndex];
					
					let outerContentView = CustomUIButton(frame: featuredFrame);
					//outerContentView.backgroundColor = UIColor.gray;
					
					
					let innerContentViewContraint = CGFloat(20);
					let contentViewFrame = CGRect(x: innerContentViewContraint, y: 0, width: featuredFrame.size.width - (2*innerContentViewContraint), height: featuredFrame.size.height);
					let contentView = CustomUIButton(frame: contentViewFrame);
					
					
					let imageViewFrame = CGRect(x: 0, y: 0, width: contentViewFrame.size.width, height: contentViewFrame.size.height - 60);
					let imageView = UIImageView(frame: imageViewFrame);
					imageView.imgFromURL(sURL: currArticle.articleImages?[0] ?? "");
					imageView.contentMode = .scaleAspectFill;
					imageView.setRoundedEdge(corners: [.bottomLeft, .bottomRight, .topLeft, .topRight], radius: 5);
					
					let titleLabelFrame = CGRect(x: 0, y: imageViewFrame.size.height, width: contentViewFrame.size.width, height: 38);
					let titleLabel = UILabel(frame: titleLabelFrame);
					titleLabel.text = currArticle.articleTitle ?? "";
					titleLabel.font = UIFont(name: "SFProDisplay-Semibold", size: 22);
					titleLabel.textAlignment = .left;
					titleLabel.textColor = UIColor.black;
					//SFProText-Bold, SFProDisplay-Regular, SFProDisplay-Semibold, SFProDisplay-Black
					
					let articleCatagorytext = (currArticle.articleCatagory ?? "No Cata.") + " News";
					let articleCatagoryFrame = CGRect(x: 0, y: titleLabelFrame.size.height + imageViewFrame.size.height, width: articleCatagorytext.getWidth(withConstrainedHeight: 20, font: UIFont(name: "SFProText-Bold", size: 12)!) + 12, height: 20);
					let articleCatagory = UILabel(frame: articleCatagoryFrame);
					articleCatagory.text = articleCatagorytext;
					articleCatagory.textAlignment = .center;
					articleCatagory.textColor = .white;
					articleCatagory.backgroundColor = makeColor(r: 159, g: 12, b: 12);
					articleCatagory.font = UIFont(name: "SFProText-Bold", size: 12);
					articleCatagory.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
					
					let timeStampFrame = CGRect(x: articleCatagoryFrame.size.width, y: titleLabelFrame.size.height + imageViewFrame.size.height, width: 120, height: 20);
					let timeStamp = UILabel(frame: timeStampFrame);
					timeStamp.text = "   ∙   " + epochClass.epochToString(epoch: currArticle.articleUnixEpoch ?? -1);
					timeStamp.textAlignment = .left;
					timeStamp.textColor = UIColor.lightGray;
					timeStamp.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
					
					contentView.addSubview(timeStamp);
					contentView.addSubview(articleCatagory);
					contentView.addSubview(titleLabel);
					contentView.addSubview(imageView);
					
					outerContentView.articleCompleteData = currArticle;
					contentView.articleCompleteData = currArticle;
					
					contentView.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
					
					
					outerContentView.addSubview(contentView);
					
					outerContentView.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
					//articleImageView.layer.cornerRadius = 10;
					
					outerContentView.tag = 1;
					
					self.featuredScrollView.addSubview(outerContentView);
				}
				// change horizontal size of scrollview
				featuredScrollView.delegate = self;
				featuredScrollView.showsHorizontalScrollIndicator = true;
				featuredScrollView.backgroundColor = UIColor.white;
				
			}
			else{
				featuredMissingLabel.isHidden = false;
				featuredScrollView.isHidden = true;
			}
			
			// ASB News -----
			asbNewsPageControl.numberOfPages = asbNewsSize;
			asbNewsFrame.size = asbNewsScrollView.frame.size;
			asbNewsFrame.size.width = UIScreen.main.bounds.width - scrollViewHorizontalConstraints;
			for aIndex in 0..<asbNewsSize{
				//districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
				//districtNewsFrame.size = UIScreen.main.bounds.size;
				asbNewsFrame.origin.x = (asbNewsFrame.size.width * CGFloat(aIndex));
				
				
				// create content in scrollview
				let contentView = UIView(frame: asbNewsFrame); // wrapper for article
				//contentView.backgroundColor = UIColor.gray;
				
				contentView.addSubview(smallArticle(x: 0, y: 0, width: asbNewsFrame.size.width, height: 120, articleSingle: asbArticlePairs[aIndex][0]));
				if (asbArticlePairs[aIndex].count == 2){
					// B button
					contentView.addSubview(smallArticle(x: 0, y: 120, width: asbNewsFrame.size.width, height: 120, articleSingle: asbArticlePairs[aIndex][1]));
				}
				
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
				sportsNewsFrame.origin.x = (sportsNewsFrame.size.width * CGFloat(aIndex));
				
				
				
				// create content in scrollview
				let contentView = UIView(frame: sportsNewsFrame); // wrapper for article
			
				contentView.addSubview(smallArticle(x: 0, y: 0, width: sportsNewsFrame.size.width, height: 120, articleSingle: sportsArticlePairs[aIndex][0]));
				
				if (sportsArticlePairs[aIndex].count == 2){
					// B button
					contentView.addSubview(smallArticle(x: 0, y: 120, width: sportsNewsFrame.size.width, height: 120, articleSingle: sportsArticlePairs[aIndex][1]));
				}
				
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
				districtNewsFrame.origin.x = (districtNewsFrame.size.width * CGFloat(aIndex));
				
				// create content in scrollview
				let contentView = UIView(frame: districtNewsFrame); // wrapper for article
				contentView.addSubview(smallArticle(x: 0, y: 0, width: districtNewsFrame.size.width, height: 120, articleSingle: districtArticlePairs[aIndex][0]));
				
				if (districtArticlePairs[aIndex].count == 2){
					// B button
					contentView.addSubview(smallArticle(x: 0, y: 120, width: districtNewsFrame.size.width, height: 120, articleSingle: districtArticlePairs[aIndex][1]));
				}
				
				self.districtNewsScrollView.addSubview(contentView);
			}
			// change horizontal size of scrollview
			districtNewsScrollView.contentSize = CGSize(width: (districtNewsFrame.size.width * CGFloat(districtNewsSize)), height: districtNewsScrollView.frame.size.height);
			districtNewsScrollView.delegate = self;
		}
	}
	
	@objc func refreshAllArticles(){
		print("refresh");
		//article.setUpLocalData();
		featuredLabel.text = loading;
		asbLabel.text = loading;
		sportsLabel.text = loading;
		districtLabel.text = loading;
		getHomeArticleData();
	}
	
	override func viewDidLoad() { // setup function
		super.viewDidLoad();
		
		featuredLabel.text = loading;
		asbLabel.text = loading;
		sportsLabel.text = loading;
		districtLabel.text = loading;
		
		mainScrollView.alwaysBounceVertical = true;
	  	getHomeArticleData();
		refreshControl.addTarget(self, action: #selector(refreshAllArticles), for: UIControl.Event.valueChanged);
		mainScrollView.addSubview(refreshControl);
		mainScrollView.delegate = self;
	}
	
	override func viewDidAppear(_ animated: Bool) {
		refreshControl.didMoveToSuperview();
	}
	
	func  scrollViewDidScroll(_ scrollView: UIScrollView) {
		if (scrollView.tag != -1){
			
			asbNewsPageControl.currentPage = Int(round(asbNewsScrollView.contentOffset.x / asbNewsFrame.size.width));
			
			sportsNewsPageControl.currentPage = Int(round(sportsNewsScrollView.contentOffset.x / sportsNewsFrame.size.width));
			
			districtNewsPageControl.currentPage = Int(round(districtNewsScrollView.contentOffset.x / districtNewsFrame.size.width));
		}
	}
	
	
}

