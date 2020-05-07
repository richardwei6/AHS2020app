//
//  File.swift
//  AHS20
//
//  Created by Richard Wei on 4/21/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox

class articlePageViewController: UIViewController, UIScrollViewDelegate{
    /*@IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var whiteBackground: UIImageView!*/    //@IBOutlet weak var notificationBellButton: UIButton!
   
    @IBOutlet weak var mainScrollView: UIScrollView!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var whiteBackground: UIImageView!
    
    @IBOutlet weak var bookmarkButton: CustomUIButton!
    
    var contentWidth: CGFloat = 0.0
    var imageFrame = CGRect(x: 0, y:0, width: 0, height: 0);
    var imageSize = 3;
    
    func setUpColorOfBookmark(sender: CustomUIButton){
        if (isSavedCurrentArticle(articleID: sender.articleID ?? "") == true){ // TODO: implement sender.articleID
            sender.tintColor = mainThemeColor;
        //    sender.backgroundColor = mainThemeColor;
        }
        else{
            sender.tintColor = UIColor.white;
        //    sender.backgroundColor = nil; // clear bacgkround color
        }
    }
    
    @IBAction func saveArticle(sender: CustomUIButton){
        print("Bookmark");
        if (sender.isSelected == false){
            sender.tintColor = mainThemeColor;
            saveCurrentArticle(articleID: sender.articleID ?? ""); // TODO: change ?? to ! instead
        }
        else{
            sender.tintColor = UIColor.white;
            removeCurrentArticle(articleID: sender.articleID ?? ""); // TODO: change ?? to ! instead
        }
        sender.isSelected = !sender.isSelected;
    }
    
    @IBAction func exitArticle(_ sender: UIButton){
        dismiss(animated: true);
       AudioServicesPlaySystemSound(1519);
    }
    
    
    // ------------
    // TODO: Fix issue where long text gets cut off *completed, thank u for the reminder XD -em&kim
    // TODO: Fix issue where the imagescrollview doesn't allow you to go to the third image on real devices -fixed
    // TODO: Fix issue on Storyboard where the Bookmark button gets blurry - use the system bookmark image and put a circular background behind it *completed
    // ------------
    override func viewDidLoad() {
        super.viewDidLoad();
        
        mainScrollView.topAnchor.constraint(equalToSystemSpacingBelow: view.topAnchor, multiplier: 1).isActive = true;
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        setUpColorOfBookmark(sender: bookmarkButton);
    
        articleText.text = "Opera nullo ratio an libet de tangi sequi. Im me gurgitem quadrati connivet experiar de fatendum quatenus. Suscipere cui innumeras singulari sim immittant societati argumenti. Proponere concipiam evidentia purgantur to ne vereorque ac. Corpo to nihil nolim prima et et ad. Verti est supra imo omnem sic sitas Cum facultate supersunt objective spectatum nul meditatio jam suo. Possum sacras initia rea ita. Illud ferre sub gustu tes agi solum. Rem cogitari mutuatur pla attentum. Me quandiu ac is id intueor ineptum. Prorsus fraudem certius agnosco eo sirenes dicitur gi. Nulli tangi is omnem ei ex at. Vos conservet via existendi nia conflatum admiserim eas dubitavit. To et existat quosdam equidem ac affirmo formali accepit.Viderer totaque ineptum id ac et. Eaedem vi fueram to du at mentes. Confirmari praesertim praecipuis ex externarum ac at satyriscos to. Vitae etc lumen lus solam novas lapis. Ha exhibentur occasionem credidique si sufficeret. Creatus idearum admonet reducit ne si in quandam cognitu. Quid veat mens eas cui rem.Hactenus animalia existimo potentia rea ita perpauca existens. Existimo reductis nonnihil fal inficior sui his via.Opera nullo ratio an libet de tangi sequi. Im me gurgitem quadrati connivet experiar de fatendum quatenus. Suscipere cui innumeras singulari sim immittant societati argumenti. Proponere concipiam evidentia purgantur to ne vereorque ac. Corpo to nihil nolim prima et et ad. Verti est supra imo omnem sic sitas Cum facultate supersunt objective spectatum nul meditatio jam suo. Possum sacras initia rea ita. Illud ferre sub gustu tes agi solum. Rem cogitari mutuatur pla attentum. Me quandiu ac is id intueor ineptum. Prorsus fraudem certius agnosco eo sirenes dicitur gi. Nulli tangi is omnem ei ex at. Vos conservet via existendi nia conflatum admiserim eas dubitavit. To et existat quosdam equidem ac affirmo formali accepit.Viderer totaque ineptum id ac et. Eaedem vi fueram to du at mentes. Confirmari praesertim praecipuis ex externarum ac at satyriscos to. Vitae etc lumen lus solam novas lapis. Ha exhibentur occasionem credidique si sufficeret. Creatus idearum admonet reducit ne si in quandam cognitu. Quid veat mens eas cui rem.Hactenus animalia existimo potentia rea ita perpauca existens. Existimo reductis nonnihil fal inficior sui his via. Opera nullo ratio an libet de tangi sequi. Im me gurgitem quadrati connivet experiar de fatendum quatenus. Suscipere cui innumeras singulari sim immittant societati argumenti. Proponere concipiam evidentia purgantur to ne vereorque ac. Corpo to nihil nolim prima et et ad. Verti est supra imo omnem sic sitas Cum facultate supersunt objective spectatum nul meditatio jam suo. Possum sacras initia rea ita. Illud ferre sub gustu tes agi solum. Rem cogitari mutuatur pla attentum. Me quandiu ac is id intueor ineptum. Prorsus fraudem certius agnosco eo sirenes dicitur gi. Nulli tangi is omnem ei ex at. Vos conservet via existendi nia conflatum admiserim eas dubitavit. To et existat quosdam equidem ac affirmo formali accepit.Viderer totaque ineptum id ac et. Eaedem vi fueram to du at mentes. Confirmari praesertim praecipuis ex externarum ac at satyriscos to. Vitae etc lumen lus solam novas lapis. Ha exhibentur occasionem credidique si sufficeret. Creatus idearum admonet reducit ne si in quandam cognitu. Quid veat mens eas cui rem.Hactenus animalia existimo potentia rea ita perpauca existens. Existimo reductis nonnihil fal inficior sui his via.Opera nullo ratio an libet de tangi sequi. Im me gurgitem quadrati connivet experiar de fatendum quatenus. Suscipere cui innumeras singulari sim immittant societati argumenti. Proponere concipiam evidentia purgantur to ne vereorque ac. Corpo to nihil nolim prima et et ad. Verti est supra imo omnem sic sitas Cum facultate supersunt objective spectatum nul meditatio jam suo. Possum sacras initia rea ita. Illud ferre sub gustu tes agi solum. Rem cogitari mutuatur pla attentum. Me quandiu ac is id intueor ineptum. Prorsus fraudem certius agnosco eo sirenes dicitur gi. Nulli tangi is omnem ei ex at. Vos conservet via existendi nia conflatum admiserim eas dubitavit. To et existat quosdam equidem ac affirmo formali accepit.Viderer totaque ineptum id ac et. Eaedem vi fueram to du at mentes. Confirmari praesertim praecipuis ex externarum ac at satyriscos to. Vitae etc lumen lus solam novas lapis. Ha exhibentur occasionem credidique si sufficeret. Creatus idearum admonet reducit ne si in quandam cognitu. Quid veat mens eas cui rem.Hactenus animalia existimo potentia rea ita perpauca existens. Existimo reductis nonnihil fal inficior sui his via."
        
        //rounded corners (bottom corners-> [.layerMaxXMaxYCorner, .layerMinXMaxYCorner])
        whiteBackground.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        
        //horizontal image scroll view
       /* imageScrollView.delegate = self
        
        for imageIndex in 0..<imageSize{
            let imageToDisplay = UIImage(named: "\(imageIndex).png")
            let imageView = UIImageView(image: imageToDisplay)
            
            imageScrollView.addSubview(imageView)
            
            let xCoordinate = view.frame.midX + view.frame.width * CGFloat(imageIndex) // use UIScreen.main.bounds.width instead - view.frame.width is constant size across all devices while UIScreen is different
            
            contentWidth += view.frame.width
            
            imageView.frame = CGRect(x: xCoordinate, y: view.frame.height/2, width: 100, height: 100)
        }
        
        imageScrollView.contentSize = CGSize(width: contentWidth, height: view.frame.height)*/
        
        imagePageControl.numberOfPages = imageSize;
        imageFrame.size = imageScrollView.frame.size;
        imageFrame.size.width = UIScreen.main.bounds.size.width;
        for imageIndex in 0..<imageSize{
            imageFrame.origin.x = (imageFrame.size.width * CGFloat(imageIndex));
            
            let imageView = UIImageView(frame: imageFrame);
            //imageView.backgroundColor = UIColor.white;
            // add image here
            
            self.imageScrollView.addSubview(imageView);
        }
        imageScrollView.contentSize = CGSize(width: (imageFrame.size.width * CGFloat(imageSize)), height: imageScrollView.frame.size.height);
        imageScrollView.delegate = self;
        
    }
    
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        imagePageControl.currentPage = Int(imageScrollView.contentOffset.x / imageFrame.size.width);
    }
}
