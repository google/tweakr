import {AngularFireAuth} from '@angular/fire/auth';
import {AngularFireDatabase} from '@angular/fire/database';
import {auth} from 'firebase/app';

import { Observable, of, Subscription } from 'rxjs';

/**
 * Mocks the Firebase auth by automatically logging in.
 */
export const AngularFireAuthMock = jasmine.createSpy('signInWithEmailAndPassword')
      .and.returnValue(Promise.resolve({uid: 'fakeuser'}));

/**
 * Mocks an AngularFireDatabase that always returns the given data for any path.
 */
export function mockAngularFireDatabase(data): AngularFireDatabase {
  return {
    object: (path: string): any => {
      return {
        valueChanges() {
          return of(data);
        }
      }
    }
  } as AngularFireDatabase;
}
