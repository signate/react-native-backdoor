# react-native-backdoor

![stability-wip](https://img.shields.io/badge/stability-work_in_progress-lightgrey.svg)

Calabash backdoor for react native applications

# Install

Install `react-native-backdoor` using:

``npm install --save signate/react-native-backdoor#master``

## Linking project

#### Android

Go to `settings.gradle` inside your android project folder and paste this lines there:

```java
include ':eact-native-backdoor'

project(':react-native-backdoor').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-backdoor/android')
```

and paste it into `build.gradle`:

```java
implementation project(path: ':react-native-backdoor')
```

In your `MainApplication.java` add:
```java
import com.signate.android.react.backdoor.Backdoor;
import com.signate.android.react.backdoor.BackdoorPackage;

public class MainApplication extends Application implements ReactApplication {
  private Backdoor mBackdoor = null;

  // ...

  protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
		  // ...
          new BackdoorPackage(mBackdoor),
      );
  }

  public Object getBackdoor() {
    return mBackdoor;
  }

  @Override
  public void onCreate() {
	// ...
    mBackdoor = new Backdoor();
  }

  // ...
}
```

# How to use?

Here is a few use cases:

```javascript
import backdoor from 'react-native-backdoor';

// define backdoor methods

// simple backdoor
backdoor.getSessionId = () => {
	return '<session-id>';
};

// async backdoor with params
backdoor.login = ([login, token], resolve) => {
	Session.login(login, token)
    	.then(() => resolve(true));
        .catch(() => resolve(false));
};

// async backdoor
backdoor.invalidateCache = (args, resolve) => {
	Cache.invalidate().then(resolve)
};
```

# Using backdoors in calabash

## Android

ReactNativeBackdoor module (`/calabash/react_native_backdoor_android.rb`) provides a very clean API to call backdoor:

```ruby
sessionId = ReactNativeBackdoor.invoke('getSessionId')
isLoginSuccessful = ReactNativeBackdoor.invoke('login', 'username', 'token');
# etc
```
