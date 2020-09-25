import { ActivatedRoute } from '@angular/router';
import {environment} from '../environments/environment';

function getParam(paramKey: string): string {
  const re = new RegExp(paramKey + '=([^&]+)', 'i');
  const keyMatches = re.exec(window.location.href);
  if (keyMatches) {
    return decodeURIComponent(keyMatches[1]);
  }

  return null;
}

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
