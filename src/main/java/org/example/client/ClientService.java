package org.example.client;

import io.grpc.stub.StreamObserver;
import org.numbers.NumberResponse;

import java.util.concurrent.atomic.AtomicLong;

public class ClientService implements StreamObserver<NumberResponse> {
    private final AtomicLong lastVal = new AtomicLong(0);

    @Override
    public void onNext(NumberResponse numberResponse) {
        setLastVal(numberResponse.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onCompleted() {
    }

    private void setLastVal(long value) {
        lastVal.set(value);
    }

    public long getLastValueAndReset() {
        return lastVal.getAndSet(0);
    }

}

