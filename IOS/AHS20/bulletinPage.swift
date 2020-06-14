//
//  bulletinPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import Firebase
import FirebaseDatabase



class bulletinClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {
    
    @IBOutlet weak var filterScrollView: UIScrollView!
    @IBOutlet weak var bulletinScrollView: UIScrollView!
    
    
    @IBOutlet weak var filterScrollViewHeightContraint: NSLayoutConstraint!
    
    struct bulletinArticleData: Codable {
        var articleID: String?;
        var articleTitle: String?;
        var articleUnixEpoch: Int64?; // unix epoch time stamp
        var articleBody: String?;
        var articleAuthor: String?;
        var articleImages: [String]?; // list of image urls
        var articleType = -1;
    }
    
    let refreshControl = UIRefreshControl();
    let seniorYearConst = "21";
    
    // padding variables
    let iconHorizontalPadding = CGFloat(20);
    let articleHorizontalPadding = CGFloat(10);
    let articleVerticalPadding = CGFloat(10);
    let articleVerticalSize = CGFloat(130);
    
    
    let filterSize = 6;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    var filterIconImageSize = CGFloat(40);
    
    
    let filterIconPicturePath = ["Group 51.png","Path 275.png","Group 34.png","Path 276.png","Path 277.png"];
    let filterIconName = ["Seniors", "Colleges", "Events", "Athletics", "Reference", "Others"];
    
    var selectedFilters: [Bool] = [false, false, false, false, false, false]; // selected types in this order - seniors, colleges, events, athletics, reference, and others
    
    var totalArticles = [bulletinArticleData]();
    
    // icon stuff
    var iconViewFrame: CGRect?;
    var filterScrollViewMaxHeight: CGFloat?;
    var filterScrollViewMinHeight: CGFloat?;
    
    func getBulletinArticleData() {
        setUpConnection();
        if (internetConnected){
            print("ok -------- loading articles - bulletin");
            
            bulletinArticleList = [[bulletinArticleData]](repeating: [bulletinArticleData](), count: 6);
            for i in 0..<6{
                var s: String; // path inside homepage
                switch i {
                case 0: // seniors
                    s = "seniors";
                    break;
                case 1: // colleges
                    s = "colleges";
                    break;
                case 2: // events
                    s = "events";
                    break;
                case 3: // athletics
                    s = "athletics";
                    break;
                case 4: // reference
                    s = "reference";
                    break;
                case 5: // others
                    s = "others";
                    break;
                default:
                    s = "";
                    break;
                }
                
                ref.child("bulletin").child(s).observeSingleEvent(of: .value) { (snapshot) in
                  //  print(s);
                    
                  //  print(snapshot.childrenCount)
                    let enumerator = snapshot.children;
                    var temp = [bulletinArticleData](); // temporary array
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        //print(article);
                        
                        
                        let enumerator = article.children;
                        var singleArticle = bulletinArticleData();
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                            
                            
                            if (articleContent.key == "ID"){
                                singleArticle.articleID = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleAuthor"){
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
                            //print(articleContent.key as? NSString);
                            
                        }
                        // print("append to main")
                        // print(temp.count);
                        //print(singleArticle.articleTitle);
                        //print(singleArticle.articleImages);
                        singleArticle.articleType = i;
                        temp.append(singleArticle);
                        
                    }
                    //print("append to main - ")
                    //print(temp)
                    bulletinArticleList[i] = temp;
                    self.generateBulletin();
                    self.refreshControl.endRefreshing();
                    self.addRefreshCTRL();
                };
                
            }
            
        }
        else{
            print("no network detected - bulletin");
            refreshControl.endRefreshing();
            addRefreshCTRL();
        }
        
    }
    
    @objc func openArticle(sender: CustomUIButton){
        print("Button pressed");
        let articleDataDict: [String: articleData] = ["articleContent" : sender.articleCompleteData];
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
    }
    
    @objc func addFilter(sender: CustomUIButton){
        removeAllSubViews(sender: sender);
        if (sender.isSelected){ // selected to unselected
            sender.backgroundColor = nil;
        }else{ // unselected to selected
            sender.backgroundColor = makeColor(r: 127, g: 47, b: 60);
        }
        sender.isSelected = !sender.isSelected;
        selectedFilters[sender.articleIndex] = sender.isSelected;
        //print(sender.isSelected);
        generateIconImage(iconView: sender);
        generateBulletin();
        // AudioServicesPlaySystemSound(1519);
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
    }
    
    func removeAllSubViews(sender: CustomUIButton){
        for view in sender.subviews{
            view.removeFromSuperview();
        }
    }
    
    
    func generateIconImage(iconView: CustomUIButton){
        if (iconView.articleIndex != 0){
            let imageIconPadding = CGFloat(5);
            let iconImageFrame = CGRect(x:(iconViewFrame!.size.width/2) - (filterIconImageSize/2) + imageIconPadding, y: (iconViewFrame!.size.height/2) - (filterIconImageSize/2)+imageIconPadding, width: filterIconImageSize-(2*imageIconPadding), height: filterIconImageSize-(2*imageIconPadding));
            let iconImageView = UIImageView(frame: iconImageFrame);
            iconImageView.image = UIImage(named: filterIconPicturePath[iconView.articleIndex-1]);
            iconImageView.contentMode = .scaleAspectFit;
            if (iconView.isSelected == true){
                iconImageView.setImageColor(color: UIColor.white);
            }
            else{
                iconImageView.setImageColor(color: mainThemeColor);
            }
            iconView.addSubview(iconImageView);
        }else{
            let yearTextFrame = CGRect(x: 0, y: 5, width: iconViewFrame!.size.width, height: iconViewFrame!.size.height);
            let yearText = UILabel(frame: yearTextFrame);
            yearText.text = "20\n21";
            yearText.setLineSpacing(lineHeightMultiple: 0.7);
            if (iconView.isSelected == true){
                yearText.textColor = UIColor.white;
            }
            else{
                yearText.textColor = mainThemeColor;
            }
            yearText.numberOfLines = 2;
            yearText.textAlignment = .center;
            yearText.font = UIFont(name: "HarlowSolid", size: 20);
            iconView.addSubview(yearText);
        }
    }
    
    
    func filterArticles() -> [bulletinArticleData]{
        var copy = [bulletinArticleData]();
        
        for i in 0..<totalArticles.count{
            if (selectedFilters[totalArticles[i].articleType] == true){
                copy.append(totalArticles[i]);
            }
        }
        return copy.count == 0 ?totalArticles:copy;
    }
    
    func addRefreshCTRL(){
        refreshControl.addTarget(self, action: #selector(refreshBulletin), for: UIControl.Event.valueChanged);
        bulletinScrollView.addSubview(refreshControl);
        bulletinScrollView.isScrollEnabled = true;
        bulletinScrollView.alwaysBounceVertical = true;
    }
    
    
    func bulletinDataToarticleData(data: bulletinArticleData) -> articleData{
        var temp = articleData();
        temp.articleAuthor = data.articleAuthor;
        temp.articleBody = data.articleBody;
        temp.articleUnixEpoch = data.articleUnixEpoch;
        temp.articleID = data.articleID;
        temp.articleImages = data.articleImages;
        temp.articleTitle = data.articleTitle;
        return temp;
    }
    
    func generateBulletin(){ // TODO: implement filter ---------
        // set up bulletin
        
        // remove all prev views
        if (bulletinArticleList[0].count > 0 && bulletinArticleList[1].count > 0 && bulletinArticleList[2].count > 0 && bulletinArticleList[3].count > 0 && bulletinArticleList[4].count > 0 && bulletinArticleList[5].count > 0){
      
            totalArticles = [bulletinArticleData]();
            for i in 0...5{
                for c in bulletinArticleList[i]{
                    totalArticles.append(c);
                }
            }
            
            for subview in bulletinScrollView.subviews{
                subview.removeFromSuperview();
            }
            
            var currentArticles = filterArticles();
            
            var bulletinSize = currentArticles.count;
            var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
            
            bulletinFrame.size.height = articleVerticalSize;
            bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
            
            let imageArticleSize = CGFloat(35);
            
            for aIndex in 0..<bulletinSize{
                bulletinFrame.origin.x = articleHorizontalPadding;
                bulletinFrame.origin.y = articleVerticalPadding+((bulletinFrame.size.height+articleVerticalPadding)*CGFloat(aIndex));
                let articleButton = UIView(frame: bulletinFrame);
                articleButton.backgroundColor = UIColor.white;
                
                // content inside button
                let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding), height: bulletinFrame.size.height - 10);
                let mainView = CustomUIButton(frame: mainViewFrame);
                
                
                if (currentArticles[aIndex].articleType != 0){
                    let articleIconFrame = CGRect(x: 2, y: 7, width: imageArticleSize, height: imageArticleSize);
                    let articleIcon = UIImageView(frame: articleIconFrame);
                    articleIcon.image = UIImage(named: filterIconPicturePath[currentArticles[aIndex].articleType-1]); // temporary------
                    articleIcon.contentMode = .scaleAspectFit;
                    articleIcon.setImageColor(color: mainThemeColor);
                    mainView.addSubview(articleIcon);
                }
                else{
                    let articleIconFrame = CGRect(x: 2, y: 7, width: 30, height: 50);
                    let articleIcon = UILabel(frame: articleIconFrame);
                    articleIcon.text = "20\n" + seniorYearConst;
                    articleIcon.setLineSpacing(lineHeightMultiple: 0.7);
                    articleIcon.numberOfLines = 3;
                    articleIcon.textAlignment = .center;
                    articleIcon.font = UIFont(name: "HarlowSolid", size: 22);
                    articleIcon.textColor = mainThemeColor;
                    mainView.addSubview(articleIcon);
                }
                //articleIcon.setImageColor(color: mainThemeColor);
                
                let articleTitleFrame = CGRect(x: 45, y : 17, width: UIScreen.main.bounds.size.width - articleHorizontalPadding - 100, height: 25);
                let articleTitleText = UILabel(frame: articleTitleFrame);
                articleTitleText.text = currentArticles[aIndex].articleTitle; // insert title text here ------ temporary
                articleTitleText.font =  UIFont(name: "SFProText-Bold",size: 20);
                //articleTitleText.numberOfLines = 1;
                //articleTitleText.backgroundColor = UIColor.gray;
                articleTitleText.adjustsFontSizeToFitWidth = true;
                articleTitleText.minimumScaleFactor = 0.5;
                
                
                
                let articleBodyFrame = CGRect(x: 0, y: 44, width: mainViewFrame.size.width, height: mainViewFrame.size.height - 50);
                let articleBodyText = UILabel(frame: articleBodyFrame);
                articleBodyText.text = currentArticles[aIndex].articleBody;// insert body text here ------- temporary
                articleBodyText.numberOfLines = 4;
                articleBodyText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
                
                
                
                mainView.addSubview(articleTitleText);
                //mainView.addSubview(dateText);
                mainView.addSubview(articleBodyText);
                
                let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 95, y : 5, width: 100, height: 25);
                let dateText = UILabel(frame: dateTextFrame);
                dateText.text = epochClass.epochToDateString(epoch: currentArticles[aIndex].articleUnixEpoch ?? -1); // insert date here -------- temporary
                dateText.textColor = makeColor(r: 189, g: 151, b: 104);
                dateText.textAlignment = .right;
                
                
                articleButton.addSubview(dateText);
                articleButton.addSubview(mainView);
                
                articleButton.layer.cornerRadius = 10;
                
                mainView.articleCompleteData = bulletinDataToarticleData(data: currentArticles[aIndex]);
                
                mainView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
                self.bulletinScrollView.addSubview(articleButton);
            }
            bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width, height: 2*articleVerticalPadding+(bulletinFrame.size.height+articleVerticalPadding)*CGFloat(bulletinSize)+75);
            bulletinScrollView.delegate = self;
            addRefreshCTRL();
        }
    }
    
    @objc func refreshBulletin(){
        print("refresh");
        // add func to load data
        getBulletinArticleData();
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();

        
        filterScrollViewMaxHeight = 89;
        filterScrollViewMinHeight = 20;
        
        // set up both scrollviews here
        
        let textMargin = CGFloat(20);
        let topMargin = CGFloat(10);
        let betweenMargin = CGFloat(3);
        
        
        let filterIconSize = filterScrollView.frame.size.height-textMargin-topMargin-betweenMargin;
        iconViewFrame = CGRect(x:0, y:topMargin, width: filterIconSize, height: filterIconSize);
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterScrollView.frame.size.height;
        filterFrame.size.width = filterScrollView.frame.size.height; //
        
        for buttonIndex in 0..<filterSize{
            filterFrame.origin.x = 15+((filterIconSize+iconHorizontalPadding) * CGFloat(buttonIndex));
            
            
            let mainView = UIView(frame: filterFrame);
            
            let iconView = CustomUIButton(frame: iconViewFrame!);
            iconView.layer.cornerRadius = filterIconSize/2;
            iconView.layer.borderColor = makeColor(r: 127, g: 47, b: 60).cgColor;
            iconView.layer.borderWidth = 3;
            iconView.articleIndex = buttonIndex;
            
            
            generateIconImage(iconView: iconView);
            
            
            let iconTextFrame = CGRect(x:0, y: filterIconSize+topMargin+betweenMargin, width: filterIconSize, height: textMargin);
            let iconText = UILabel(frame: iconTextFrame);
            iconText.text = filterIconName[buttonIndex];
            iconText.numberOfLines = 1;
            iconText.font = UIFont(name: "SFProDisplay-Regular", size: 13);
            iconText.textAlignment = .center;
            
            
            iconView.addTarget(self, action: #selector(self.addFilter), for: .touchUpInside);
            
            mainView.addSubview(iconView);
            mainView.addSubview(iconText);
            
            
            self.filterScrollView.addSubview(mainView);
        }
        filterScrollView.contentSize = CGSize(width: (filterIconSize+iconHorizontalPadding) * CGFloat(filterSize)+15, height: filterScrollView.frame.size.height);
        filterScrollView.delegate = self;
        
        
        // set up bulletin for the first time before any filters
        getBulletinArticleData();
        
    }
    
}
