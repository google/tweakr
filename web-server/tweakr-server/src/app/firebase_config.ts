import { ActivatedRoute } from '@angular/router';
import {environment} from '../environments/environment';

/**
 * Returns the value of the given query param.
 */
function getParam(paramKey: string): string {
  const re = new RegExp(paramKey + '=([^&]+)', 'i');
  const keyMatches = re.exec(window.location.href);
  if (keyMatches) {
    return decodeURIComponent(keyMatches[1]);
  }

  return null;
}

/**
 * Loads the Firebase config from either the URL query params or the environment
 * variables. This allows Easyserver to dynamically load Firebase configs from
 * the URL, instead of having to set up your own server environment.
 */
export function getFirebaseConfig(route?: ActivatedRoute): any {
  const config = getParam('firebase');
  if (config) {
    try {
      return JSON.parse(config);
    } catch (e) {
      console.error('Failed to parse Firebase config', e);
      return null;
    }
  }

  if (environment.firebase && environment.firebase.apiKey != 'YOUR_INFO_HERE') {
    return environment.firebase;
  }

  return null;
}
