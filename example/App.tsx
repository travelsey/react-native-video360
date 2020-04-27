/**
 * Sample VideoView360
 * https://github.com/facebook/react-native
 *
 *
 * Salif Omar Faye
 * Company : Stride srls
 * Website : https://stride-it.com
 * Email   : info@stride-it.com
 *
 * @format
 */

import React, { Component } from "react";
import { SafeAreaView, StyleSheet, StatusBar, View, Text } from "react-native";
import { Colors } from "react-native/Libraries/NewAppScreen";
import Video360 from "react-native-video360plugin";

const videoPath = require("./assets/vv360.mp4");
declare const global: { HermesInternal: null | {} };

interface State {
  videoUrl: string;
  pathUrl: string;
  modeVideo: number;
}
export default class App extends Component {
  state: State = {
    videoUrl: "https://vimeo.com/214402865",
    pathUrl: "./assets/vv360.mp4",
    modeVideo: 1,
  };

  render = () => {
    return (
      <>
        <StatusBar barStyle="dark-content" />
        <SafeAreaView>
          <View
            style={{ width: "100%", height: "100%", backgroundColor: "blue" }}
          >
            <Video360
              style={{ flex: 1 }}
              urlVideo={this.state.videoUrl}
              modeVideo={this.state.modeVideo}
            />
          </View>
        </SafeAreaView>
      </>
    );
  };
}

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: "absolute",
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: "600",
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: "400",
    color: Colors.dark,
  },
  highlight: {
    fontWeight: "700",
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: "600",
    padding: 4,
    paddingRight: 12,
    textAlign: "right",
  },
});
