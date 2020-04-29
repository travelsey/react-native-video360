//  Created by davichoso@gmail.com

#import "Video360.h"
#import "PlayerViewController.h"


@implementation Video360 {
  PlayerViewController *_playerView;
}

- (instancetype)init
{
  self = [super init];
  if (self) {
    _playerView = [[PlayerViewController alloc] init];
  }
  return self;
}

- (void)layoutSubviews
{
  [super layoutSubviews];
  _playerView.view.frame = self.bounds;
  [self addSubview:_playerView.view];
  
}
- (void)setUrlVideo:(NSString *)urlVideo
{
  if(![	urlVideo isEqual: _urlVideo	 ])
  {
    _urlVideo = urlVideo;
    NSLog(@"%@", urlVideo);
    _playerView.urlVideo = urlVideo;
  }
}

- (void)setModeVideo:(int)modeVideo
{
  if( modeVideo != _modeVideo  )
  {
    _modeVideo= modeVideo;
    if(modeVideo == 1)
      _playerView.demoType = DemoType_AVPlayer_VR;
    if(modeVideo == 2)
      _playerView.demoType = DemoType_AVPlayer_VR_Box;
    if(modeVideo == 3)
      _playerView.demoType = DemoType_AVPlayer_Normal;
  }
}






@end
