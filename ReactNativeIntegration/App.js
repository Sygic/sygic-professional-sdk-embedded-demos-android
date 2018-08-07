import React from 'react';
import { StyleSheet, Text, View, NativeModules, Button } from 'react-native';

const sygicNavi = NativeModules.SygicNavigation;

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Button
            onPress={() => sygicNavi.start()}
            title='Start navigation'
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
