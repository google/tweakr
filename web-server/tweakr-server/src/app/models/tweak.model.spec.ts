import {createTestTweak} from 'src/app/testing/model-utils';
import {Tweak} from './tweak.model';

describe('Tweak Model', () => {

  it('should create', () => {
    const tweak = createTestTweak('float', 'Test:Tweaky');
    expect(tweak).toBeTruthy();
    expect(tweak.type).toBe('float');
    expect(tweak.id).toBe('Test:Tweaky');
  });

  it('set should not update value', () => {
    const tweak = createTestTweak('float', 'Test:Tweak', {
      initialValue: 0.5
    });

    expect(tweak.initialValue).toBe(0.5);
    expect(tweak.value).toBe(0.5);

    tweak.set({
      value: 10,
      initialValue: 20
    });

    expect(tweak.initialValue).toBe(20);
    expect(tweak.value).toBe(0.5);
  });


  it('set should not clobber description', () => {
    const tweak = createTestTweak('float', 'Test:Tweak', {
      initialValue: 0.5,
      metadata: {
        description: "description"
      }
    });

    expect(tweak.metadata.description).toBe("description");

    tweak.set({
      value: 10,
      initialValue: 20
    });

    expect(tweak.metadata.description).toBe("description");
  });

  it('normalized float should have proper min/max', () => {
    const tweak = createTestTweak('float', 'Test:Tweak', {
      initialValue: 0.5
    });
    expect(tweak.initialValue).toBe(0.5);
    expect(tweak.min).toBe(0);
    expect(tweak.max).toBe(1);
  });

  it('int should have proper min/max', () => {
    const tweak = createTestTweak('int', 'Test:Tweak', {
      initialValue: 10
    });
    expect(tweak.initialValue).toBe(10);
    expect(tweak.min).toBe(0);
    expect(tweak.max).toBe(15);
  });

  it('negative int should have proper min/max', () => {
    const tweak = createTestTweak('int', 'Test:Tweak', {
      initialValue: -10
    });
    expect(tweak.initialValue).toBe(-10);
    expect(tweak.min).toBe(-15);
    expect(tweak.max).toBe(-5);
  });

  it('onChange should recalculate min/max', () => {
    const tweak = createTestTweak('int', 'Test:Tweak', {
      initialValue: 10
    });
    expect(tweak.initialValue).toBe(10);
    expect(tweak.min).toBe(0);
    expect(tweak.max).toBe(15);

    tweak.onChange(100);

    expect(tweak.initialValue).toBe(10);
    expect(tweak.min).toBe(0);
    expect(tweak.max).toBe(150);
  });

  it('onChange should call listener', () => {
    const spy = jasmine.createSpy();
    const tweak = createTestTweak('int', 'Test:Tweak', {
      initialValue: 10
    }, spy);

    tweak.onChange(100);

    expect(spy).toHaveBeenCalledWith(100);
  });

});
