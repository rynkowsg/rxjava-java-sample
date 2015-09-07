package rxjava.demo;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

@Slf4j
public class Main {

  private static Observable<Integer> generateNumbers(int howMany, int from, int to) {
    return Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {
        // prepare a pseudo-random number generator
        Random generator = new Random();
        for (int i = 0; i < howMany; ++i) {
          // get the random number
          Integer randomNumber = from + generator.nextInt(to - from + 1);
          //if (i == 6) randomNumber = 0; // couses an assertion
          // if it is incorrect, emit an AssertionError
          if (randomNumber < from || randomNumber > to) {
            String message =
                String.format("Number %d doesn't belong to range <%d. %d>", randomNumber, from, to);
            subscriber.onError(new AssertionError(message));
          }
          // if it is correct, emit the number
          subscriber.onNext(randomNumber);
        }
        // at the end emit an information about end of events.
        subscriber.onCompleted();
      }
    });
  }

  private static class NumbersObserver implements Observer<Integer> {
    @Override public void onCompleted() {
      log.info("Completed.");
    }

    @Override public void onError(Throwable e) {
      log.error("Error: {}", e.getMessage());
    }

    @Override public void onNext(Integer integer) {
      log.info("Received: {}", integer);
    }
  }

  public static void main(String[] args) {
    Integer howMany, from, to;
    generateNumbers(howMany = 10, from = 20, to = 100).subscribe(new NumbersObserver());
  }
}
