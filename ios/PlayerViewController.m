
#import "PlayerViewController.h"
#import <SGPlayer/SGPlayer.h>

@interface PlayerViewController ()

@property (nonatomic, strong) SGPlayer * player;
@property (weak, nonatomic) IBOutlet UISlider *progressSilder;
@property (nonatomic, assign) BOOL progressSilderTouching;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *playButton;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *pauseButton;
@property UIImage* playImage;
@property UIImage* pauseImage;
@end

@implementation PlayerViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor blackColor];
    self.playImage = [UIImage imageNamed:@"Play"];
    self.pauseImage = [UIImage imageNamed:@"Pause"];
    
    self.player = [SGPlayer player];
    [self.player registerPlayerNotificationTarget:self
                                      stateAction:@selector(stateAction:)
                                   progressAction:@selector(progressAction:)
                                   playableAction:@selector(playableAction:)
                                      errorAction:@selector(errorAction:)];
    [self.player setViewTapAction:^(SGPlayer * _Nonnull player, SGPLFView * _Nonnull view) {
        NSLog(@"player display view did click!");
    }];
    [self.view insertSubview:self.player.view atIndex:0];
    [self playVideFromUrl:[NSURL URLWithString:self.urlVideo]];
    [self.playButton setImage:_playImage forState:UIControlStateNormal];
    [self.pauseButton setImage:_pauseImage forState:UIControlStateNormal];
    self.pauseButton.hidden = true;
    return;
    
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
//    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone){
//        AppDelegate* dgt = (AppDelegate*)[UIApplication sharedApplication].delegate;
//        dgt.shouldSupportPortrait = FALSE;
//
//        NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft];
//        [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
//        [UINavigationController attemptRotationToDeviceOrientation];
//    }
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}
- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
}
- (void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    
//    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone){
//        AppDelegate* dgt = (AppDelegate*)[UIApplication sharedApplication].delegate;
//        dgt.shouldSupportPortrait = TRUE;
//
//
//        NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationPortrait];
//        [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
//        [UINavigationController attemptRotationToDeviceOrientation];
//    }
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}
/*- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation{
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone){
        return UIInterfaceOrientationLandscapeLeft;
    }
    return UIInterfaceOrientationLandscapeLeft;
}*/
- (BOOL)shouldAutorotate {
    return YES;
}

- (void)playVideFromUrl:(NSURL*)vrVideo{
    
    switch (self.demoType)
    {
        case DemoType_AVPlayer_Normal:
            [self.player replaceVideoWithURL:vrVideo];
            break;
        case DemoType_AVPlayer_VR:
            [self.player replaceVideoWithURL:vrVideo videoType:SGVideoTypeVR];
            break;
        case DemoType_AVPlayer_VR_Box:
            self.player.displayMode = SGDisplayModeBox;
            [self.player replaceVideoWithURL:vrVideo videoType:SGVideoTypeVR];
            break;
        case DemoType_FFmpeg_Normal:
            self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
            self.player.decoder.hardwareAccelerateEnableForFFmpeg = NO;
            [self.player replaceVideoWithURL:vrVideo];
            break;
        case DemoType_FFmpeg_Normal_Hardware:
            self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
            [self.player replaceVideoWithURL:vrVideo];
            break;
        case DemoType_FFmpeg_VR:
            self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
            self.player.decoder.hardwareAccelerateEnableForFFmpeg = NO;
            [self.player replaceVideoWithURL:vrVideo videoType:SGVideoTypeVR];
            break;
        case DemoType_FFmpeg_VR_Hardware:
            self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
            [self.player replaceVideoWithURL:vrVideo videoType:SGVideoTypeVR];
            break;
        case DemoType_FFmpeg_VR_Box:
            self.player.displayMode = SGDisplayModeBox;
            self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
            self.player.decoder.hardwareAccelerateEnableForFFmpeg = NO;
            [self.player replaceVideoWithURL:vrVideo videoType:SGVideoTypeVR];
            break;
        case DemoType_FFmpeg_VR_Box_Hardware:
            self.player.displayMode = SGDisplayModeBox;
            self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
            [self.player replaceVideoWithURL:vrVideo videoType:SGVideoTypeVR];
            break;
    }
}

- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    self.player.view.frame = self.view.bounds;
}

- (IBAction)back:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)play:(id)sender
{
    self.playButton.hidden = true;
    self.pauseButton.hidden = false;
    [self.player play];
}

- (IBAction)pause:(id)sender
{
    self.pauseButton.hidden = true;
    self.playButton.hidden = false;
    [self.player pause];
}

- (IBAction)progressTouchDown:(id)sender
{
    self.progressSilderTouching = YES;
}

- (IBAction)progressTouchUp:(id)sender
{
    self.progressSilderTouching = NO;
    [self.player seekToTime:self.player.duration * self.progressSilder.value];
}

- (void)stateAction:(NSNotification *)notification
{
    SGState * state = [SGState stateFromUserInfo:notification.userInfo];
    
    NSString * text;
    switch (state.current) {
        case SGPlayerStateNone:
            text = @"Ninguno";
            break;
        case SGPlayerStateBuffering:
            text = @"Cargando...";
            break;
        case SGPlayerStateReadyToPlay:
            text = @"Preparado";
            [self.player play];
            break;
        case SGPlayerStatePlaying:
            text = @"Reproduciendo";
            break;
        case SGPlayerStateSuspend:
            text = @"Suspendido";
            break;
        case SGPlayerStateFinished:
            text = @"Terminado";
            break;
        case SGPlayerStateFailed:
            text = @"Error";
            break;
    }
}

- (void)progressAction:(NSNotification *)notification
{
    SGProgress * progress = [SGProgress progressFromUserInfo:notification.userInfo];
    if (!self.progressSilderTouching) {
        self.progressSilder.value = progress.percent;
    }
}

- (void)playableAction:(NSNotification *)notification
{
    SGPlayable * playable = [SGPlayable playableFromUserInfo:notification.userInfo];
    NSLog(@"playable time : %f", playable.current);
}

- (void)errorAction:(NSNotification *)notification
{
    SGError * error = [SGError errorFromUserInfo:notification.userInfo];
    NSLog(@"player did error : %@", error.error);
}

- (NSString *)timeStringFromSeconds:(CGFloat)seconds
{
    return [NSString stringWithFormat:@"%ld:%.2ld", (long)seconds / 60, (long)seconds % 60];
}

- (void)dealloc
{
    [self.player removePlayerNotificationTarget:self];
}

@end
