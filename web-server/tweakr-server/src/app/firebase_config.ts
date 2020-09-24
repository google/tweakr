import { ActivatedRoute } from '@angular/router';
import {environment} from '../environments/environment';

function getParam(paramKey: string): string {
  const re = new RegExp(paramKey + '=([^&]+)', 'i');
  const keyMatches = re.exec(window.location.href);
  if (keyMatches) {
    return decodeURIComponent(keyMatches[1]);
  }
}

export function getFirebaseConfig(route?: ActivatedRoute): any {
  const config = route ? route.snapshot.paramMap.get('firebase') : getParam('firebase');
  if (config) {
    return config;
  }

  if (environment.firebase && environment.firebase.apiKey != 'YOUR_INFO_HERE') {
    return environment.firebase;
  }

  return null;
}
