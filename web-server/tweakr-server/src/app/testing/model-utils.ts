import {Tweak} from '../models/tweak.model';

export function createTestTweakFloat(id?: string, data?: {}, onChangeListener?: (value: any) => void): Tweak {
  return createTestTweak('float', id, data, onChangeListener);
}

export function createTestTweak(
    type: string,
    id: string = 'Test:Tweak',
    data?: {},
    onChangeListener: (value: any) => void = () => {}): Tweak {

  return new Tweak(id, {
    initialValue: 0.5,
    type: type,
    description: 'Test description',
    ...data
  }, onChangeListener)
}
