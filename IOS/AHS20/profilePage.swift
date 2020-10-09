//
//  profile.swift
//  AHS20
//
//  Created by Richard Wei on 9/27/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import GoogleSignIn
import AudioToolbox

var userEmail = "";
var userFullName = "";
var isSignedIn = false;
var userProfileImageURL = "";
let profileImageViewWidth = CGFloat(60);

let barcodeUserDefaultsKey = "idBarcode";

func saveBarcodeDefault(image: UIImage){
    UserDefaults.standard.register(defaults: [barcodeUserDefaultsKey:image.jpegData(compressionQuality: 100)!]);
    UserDefaults.standard.set(image.jpegData(compressionQuality: 100), forKey: barcodeUserDefaultsKey);
}
func getBarcodeDefault()->UIImage{
    var image = UIImage();
    if (UserDefaults.standard.object(forKey: barcodeUserDefaultsKey) != nil){
        if let imageData = UserDefaults.standard.value(forKey: barcodeUserDefaultsKey) as? Data{
            let imageFromData = UIImage(data: imageData)
            image = imageFromData!;
        }
    }
    else{
        print("error - getBarcodeDefault called without previous saved data");
    }
    return image;
}
func removeBarcodeDefault(){
    if (UserDefaults.standard.object(forKey: barcodeUserDefaultsKey) != nil){
        UserDefaults.standard.removeObject(forKey: barcodeUserDefaultsKey);
    }
}

func isValidStudentEmail(email: String) -> Bool{ // regex matching
    let pattern = "[0-9]{5}@students.ausd.net";
    let regex = try! NSRegularExpression(pattern: pattern);
    return regex.firstMatch(in: email, options: [], range: NSRange(location: 0, length: email.utf16.count)) != nil && email.count == 23;
}

class profilePageClass: UIViewController{
    
    @IBOutlet weak var mainScrollView: UIScrollView!
        
    @objc func signOutFunction(){
        AudioServicesPlaySystemSound(1519);
        let confirmPopup = UIAlertController(title: "Sign out", message: "You will be signed out of your account.", preferredStyle: UIAlertController.Style.actionSheet);
        confirmPopup.addAction(UIAlertAction(title: "Confirm", style: .destructive, handler: { (action: UIAlertAction!) in
            print("signed out");
            GIDSignIn.sharedInstance()?.signOut();
            userEmail = "";
            userFullName = "";
            isSignedIn = false;
            removeBarcodeDefault();
            self.renderView();
            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "rerenderHamBurgMenu"), object: nil, userInfo: nil);
        }));
        confirmPopup.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in }));
        present(confirmPopup, animated: true, completion: nil);
    }
    
    
    /*
     Interesting fact about our school's barcode. So our school's barcode uses CICode39 encoding for our ids and its why our barcode is so long. This encoder uses CICode128 which is short and can hold more data. However, the readers at school should not have any issues with different encoders.
     */
    func generateBarcode(from string: String) -> UIImage? {

        let data = string.data(using: String.Encoding.ascii)

        if let filter = CIFilter(name: "CICode128BarcodeGenerator") {
            filter.setDefaults()
            //Margin
            filter.setValue(7.00, forKey: "inputQuietSpace")
            filter.setValue(data, forKey: "inputMessage")
            //Scaling
            let transform = CGAffineTransform(scaleX: 3, y: 3)

            if let output = filter.outputImage?.transformed(by: transform) {
                let context:CIContext = CIContext.init(options: nil)
                let cgImage:CGImage = context.createCGImage(output, from: output.extent)!
                let rawImage:UIImage = UIImage.init(cgImage: cgImage)

                //Refinement code to allow conversion to NSData or share UIImage. Code here:
                //http://stackoverflow.com/questions/2240395/uiimage-created-from-cgimageref-fails-with-uiimagepngrepresentation
                let cgimage: CGImage = (rawImage.cgImage)!
                let cropZone = CGRect(x: 0, y: 0, width: Int(rawImage.size.width), height: Int(rawImage.size.height))
                let cWidth: size_t  = size_t(cropZone.size.width)
                let cHeight: size_t  = size_t(cropZone.size.height)
                let bitsPerComponent: size_t = cgimage.bitsPerComponent
                //THE OPERATIONS ORDER COULD BE FLIPPED, ALTHOUGH, IT DOESN'T AFFECT THE RESULT
                let bytesPerRow = (cgimage.bytesPerRow) / (cgimage.width  * cWidth)

                let context2: CGContext = CGContext(data: nil, width: cWidth, height: cHeight, bitsPerComponent: bitsPerComponent, bytesPerRow: bytesPerRow, space: CGColorSpaceCreateDeviceRGB(), bitmapInfo: cgimage.bitmapInfo.rawValue)!

                context2.draw(cgimage, in: cropZone)

                let result: CGImage  = context2.makeImage()!
                let finalImage = UIImage(cgImage: result)

                return finalImage

            }
        }

        return nil
    }

    
    @objc func renderView(){
        // set up scroll view here
        
        for view in mainScrollView.subviews{
            view.removeFromSuperview();
        }
        //mainScrollView.layer.sublayers?.forEach{ $0.removeFromSuperlayer(); };
        
        let padding = CGFloat(10);
        var scrollViewHeight = CGFloat(0);
        
        //scrollViewHeight += padding;
        
        if (isSignedIn){
            
            if (isValidStudentEmail(email: userEmail)){
                /*let barcodeWidth = CGFloat(200);
                let barcodeFrame = CGRect(x: (UIScreen.main.bounds.width/2) - (barcodeWidth/2), y: scrollViewHeight, width: barcodeWidth, height: 60);
                let barcodeView = UIImageView(frame: barcodeFrame);
                barcodeView.backgroundColor = UIColor.white;
                barcodeView.layer.cornerRadius = 5;
                barcodeView.clipsToBounds = true;
                //print("barcode - \(userEmail.substring(with: 0..<5))")
                //barcodeView.image = UIImage(barcode: userEmail.substring(with: 0..<5));
                
                if (UserDefaults.standard.object(forKey: barcodeUserDefaultsKey) == nil){
                    DispatchQueue.global(qos: .background).async { // multithreading
                        let barcodeImage = self.generateBarcode(from: userEmail.substring(with: 0..<5));
                        DispatchQueue.main.async {
                            //barcodeView.backgroundColor = UIColor.clear;
                            barcodeView.image = barcodeImage;
                            saveBarcodeDefault(image: barcodeImage!);
                        }
                    }
                }
                else{
                    barcodeView.image = getBarcodeDefault();
                }
                
                scrollViewHeight += barcodeFrame.size.height;
                
                mainScrollView.addSubview(barcodeView);
                */
                let idCardPadding = CGFloat(10);
                let idCardViewFrame = CGRect(x: idCardPadding, y: idCardPadding, width: UIScreen.main.bounds.width - 2*idCardPadding, height: UIScreen.main.bounds.width *  0.56);
                let idCardView = UIView(frame: idCardViewFrame);
                idCardView.backgroundColor = mainThemeColor;
                
                idCardView.clipsToBounds = true;
                idCardView.layer.cornerRadius = 10;
                
                let cornerPadding = CGFloat(10);
               // let nameViewFrame = CGRect(x: cornerPadding, y: cornerPadding, width: , height: )
                
                
                scrollViewHeight += idCardViewFrame.height + 2*idCardPadding;
                mainScrollView.addSubview(idCardView);
            }
            else{
                scrollViewHeight += padding;
            }
            
            let signOutButtonWidth = CGFloat(120);
            let signOutButtonFrame = CGRect(x: (UIScreen.main.bounds.width/2) - (signOutButtonWidth/2), y: scrollViewHeight, width: signOutButtonWidth, height: 40);
            let signOutButton = UIButton(frame: signOutButtonFrame);
            signOutButton.backgroundColor = UIColor.gray;
            signOutButton.layer.cornerRadius = 5;
            //signOutButton.titleLabel?.text = "Sign Out";
            signOutButton.setTitle("Sign Out", for: .normal);
            signOutButton.titleLabel?.font = UIFont(name: "SFProDisplay-Semibold", size: 15)!;
            
            signOutButton.addTarget(self, action: #selector(self.signOutFunction), for: .touchUpInside);
            
            scrollViewHeight += signOutButtonFrame.height + padding;
            
            let font = UIFont(name: "SFProDisplay-Semibold", size: 18)!;
            let widthLabelPadding = CGFloat(10);
            
            let emailLabelWidth = CGFloat(userEmail.getWidth(withConstrainedHeight: 40, font: font) + 2*widthLabelPadding);
            let emailLabelRect = CGRect(x: (UIScreen.main.bounds.width/2) - (emailLabelWidth/2), y: scrollViewHeight, width: emailLabelWidth, height: 40);
            let emailLabel = UILabel(frame: emailLabelRect);
            emailLabel.text = userEmail;
            emailLabel.font = font;
            emailLabel.backgroundColor = UIColor.gray;
            emailLabel.textColor = UIColor.white;
            emailLabel.textAlignment = .center;
            emailLabel.clipsToBounds = true;
            emailLabel.layer.cornerRadius = 5;
            
            //print("regex - \(isValidStudentEmail(email: userEmail))")
            
            scrollViewHeight += emailLabelRect.height + padding;
            
            let nameLabelWidth = CGFloat(userFullName.getWidth(withConstrainedHeight: 40, font: font) + 2*widthLabelPadding);
            let nameLabelRect = CGRect(x: (UIScreen.main.bounds.width/2) - (nameLabelWidth/2), y: scrollViewHeight, width: nameLabelWidth, height: 40);
            let nameLabel = UILabel(frame: nameLabelRect);
            nameLabel.text = userFullName;
            nameLabel.font = font;
            nameLabel.backgroundColor = UIColor.gray;
            nameLabel.textColor = UIColor.white;
            nameLabel.textAlignment = .center;
            nameLabel.clipsToBounds = true;
            nameLabel.layer.cornerRadius = 5;
            
            scrollViewHeight += nameLabelRect.height + padding;
            
            let profileImageViewFrame = CGRect(x: (UIScreen.main.bounds.width/2) - (profileImageViewWidth/2), y: scrollViewHeight, width: profileImageViewWidth, height: profileImageViewWidth);
            let profileImageView = UIImageView(frame: profileImageViewFrame);
            profileImageView.layer.cornerRadius = 5;
            profileImageView.clipsToBounds = true;
            profileImageView.backgroundColor = UIColor.gray;
            profileImageView.imgFromURL(sURL: userProfileImageURL);
            
            scrollViewHeight += profileImageViewFrame.height + padding;
            
            mainScrollView.addSubview(profileImageView);
            mainScrollView.addSubview(nameLabel);
            mainScrollView.addSubview(emailLabel);
            mainScrollView.addSubview(signOutButton);
            
        }
        else{
            let signInButtonHeight = CGFloat(80);
            let signInButton = GIDSignInButton();
            signInButton.center = CGPoint(x: UIScreen.main.bounds.width/2, y: scrollViewHeight + signInButtonHeight);
            signInButton.frame.size.height = signInButtonHeight;
            signInButton.frame.size.width = CGFloat(100);
            
            scrollViewHeight += signInButton.frame.height + signInButtonHeight;
            
            mainScrollView.addSubview(signInButton);
            
            scrollViewHeight += padding;
            
        }
        
        mainScrollView.contentSize = CGSize(width: UIScreen.main.bounds.width, height: scrollViewHeight);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.renderView), name:NSNotification.Name(rawValue: "reloadProfilePage"), object: nil);
        GIDSignIn.sharedInstance()?.presentingViewController = self;
        
        renderView();
    }
}
