import { Platform, NativeEventEmitter, NativeModules } from 'react-native';

const backdoors = {};
export default function(methods) {
    Object.assign(backdoors, methods);
}

if (!NativeModules.Backdoor) {
    if (__DEV__) {
        console.warn('Backdoor module is not registered');
    }
    
    return;
}

const eventEmitter = new NativeEventEmitter(NativeModules.Backdoor);
eventEmitter.addListener('Backdoor/invoke', function([id, name, args]) {
    invokeBackdoor(id, name, args);
});

function invokeBackdoor(id, name, args) {
    const backdoor = backdoors[name];

    if (!backdoor) {
        throw new Error(`Backdoor method "${name}" doesn't exist`);
    }

    let resolved = false;
    const returnValue = backdoor(args, resolve);

    if (returnValue !== undefined) {
        resolve(returnValue);
    }

    function resolve (value) {
        if (resolved) {
            throw new Error(`Backdoor "${name}" has already been resolved`);
        }

        NativeModules.Backdoor.resolve(id, [value]);
        resolved = true;
    }
};
