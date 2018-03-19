#### RxJava2
------
一个可以在JVM上使用， 基于事件编写可使用观察序列构成的**异步**作库。

依赖包
```java
compile 'io.reactivex.rxjava2:rxjava:2.0.1'
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
```

## 初步认识RxJava
**RxJava**的核心便是被观察者`Observable`发出一系列的事件，观察者`Observer`，通过`subscribe()`方法进行订阅接收事件并进行处理。

```java
// Observable ---> observer 通过subscribe连接
// 创建一个Observable
Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
    @Override
    public void subscribe(ObservableEmitter<String> e) throws Exception {
        e.onNext("hi");
        e.onNext("rxjava");
        e.onComplete();
    }
});

// 创建一个Observer
Observer<String> observer = new Observer<String>() {
    @Override
    public void onSubscribe(Disposable d{

    }

    @Override
    public void onNext(String value) {
        Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
};

// 使用subscribe 连接
observable.subscribe(observer);
```
>只有当调用了`subscribe()`方法后， `Observable`才会开始发送事件。
           
Observable发送事件规则

* `onNext`, **Observable**(**Observer**)可以发送（接收）无限个。
* `onComplete`和`onError`必须唯一并且互斥。
* **Observable**可以不发送`onComplete`或`onError`。
* 当**Observable**执行`onComplete`后，再发送的`onNext`，**Observer**不再接收。
	
	>当Disposable调用`dispose()`方法时，将切断Observable和Observer联系，Observable仍会发送剩余事件， Observer不再接收。
	
#### 线程切换

正常情况下，`Observable`和`Observer`在同一线程运行。
Android同一个`Activity`的所有动作默认都是在**主线程**中运行的。
>`subscribeOn()`指的是Observable发送事件的线程，`observeOn()`指的是Observer接收事件的线程。

```java
Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
  public void subscribe(ObservableEmitter<Integer> e) throws Exception {
     Log.d(TAG, "observable thread is " + Thread.currentThread().getName());
    e.onNext(1);
            }
        });
// Consumer 表示Observer只操作onNext事件
Consumer<Integer> consumer = new Consumer<Integer>() {
    @Override
  public void accept(Integer integer) throws Exception {
    Log.d(TAG, "observer thread is: " + Thread.currentThread().getName());
    Log.d(TAG, "on next" + integer);
            }
 };
 
//observable.subscribe(consumer);
        observable.subscribeOn(Schedulers.newThread())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .observeOn(Schedulers.io())
          .subscribe(consumer);
```
运行结果

```
01-25 11:03:37.000 25954-26564/rxjava2 D/GEMMILY: observable thread is RxNewThreadScheduler-7
01-25 11:03:37.000 25954-26555/rxjava2 D/GEMMILY: observer thread is: RxCachedThreadScheduler-7
01-25 11:03:37.000 25954-26555/rxjava2 D/GEMMILY: on next1
```
> `Observable`指定多次线程只有第一次生效，`Observer` 制定多次线程，每调用一次`observeOn()` 线程便会切换一次。

RxJava常用内置线程

* Schedulers.io() io线程操作，通常用于网络、读写文件等io密集型操作。
* Schedulers.computation() CPU计算密集型的操作，如需大量计算的操作
* Schedulers.newThread() 常规新线程
* AndroidSchedulers.mainThread() Android的主线程

使用场景
将一些耗时的操作放在后台, 等到操作完成之后回到主线程去更新UI。

* 网络请求
* 读写数据库
* 定时轮询

使用Retrofit网络请求
```java
RequestBody.DataBean dataBean = new RequestBody.DataBean();
dataBean.setAction("1");
dataBean.setMobile(etPhone.getText().toString());
RequestBody requestBody = new RequestBody();
requestBody.setData(dataBean);

Api api = HttpManager.create().create(Api.class);
api.request("/user/smscode", requestBody)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<JsonObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JsonObject value) {
                Toast.makeText(OnThreadActivity.this, "已发送：" + value.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OnThreadActivity.this, "发送失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });

```

如果在请求的过程中`Activity`已经销毁，此时回到主线程去更新UI会Crash。调用它的`dispose()`方法时就会切断联系，使得`Observer`收不到事件，也不会再去更新UI了。
运行过程中将`Disposable`保存起来，当`Activity`退出时，切断它即可。
>RxJava中已经内置了一个容器`CompositeDisposable`，当有多个`Disposable`时，每当得到一个`Disposable`时就调用`CompositeDisposable.add()`将它添加到容器中。在退出时，调用`CompositeDisposable.clear() `即可切断所有联系。

#### 操作符
##### Map
对`Observable`发送的每一个事件应用一个函数, 使得每一个事件都按照**指定函数**去变化。

```java
Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
```
##### FlatMap
`FlatMap`将一个发送事件的`Observable`变换为**多个**发送事件的`Observables`，然后将它们发射的事件**合并**后放进一个单独的`Observable`里。
```java
  RequestBody.DataBean dataBean = new RequestBody.DataBean();
        dataBean.setAction("1");
        dataBean.setMobile(etPhone.getText().toString());
        RequestBody requestBody = new RequestBody();
        requestBody.setData(dataBean);


        final Api api = HttpManager.create().create(Api.class);
        api.request("/user/smscode", requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<JsonObject, String>() {
                    @Override
                    public String apply(JsonObject jsonObject) throws Exception {
                        JsonObject body = jsonObject.get("body").getAsJsonObject();
                        return body.get("data").getAsString();
                    }
                }).flatMap(new Function<String, ObservableSource<JsonObject>>() {
            @Override
            public ObservableSource<JsonObject> apply(String s) throws Exception {
                LoginBody.DataBean dataBean = new LoginBody.DataBean();
                dataBean.setCode("1111");
                dataBean.setMobile(etPhone.getText().toString());
                dataBean.setSmstoken(s);
                LoginBody loginBody = new LoginBody();
                loginBody.setData(dataBean);
                return api.login("/user/login", loginBody);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject value) {
                        Toast.makeText(OnThreadActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(OnThreadActivity.this, OperatorActivity.class));

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(OnThreadActivity.this, "登陆失败", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
```
>`FlatMap`并不保证事件的顺序, 如果需要保证顺序则需要使用`concatMap`。
>

##### Simple
每隔指定的时间就从Observable中取出一个事件发送给Observer处理。
##### Zip
`Zip`通过一个函数将多个`Observable`发送的事件**按顺序结合到一起**，然后它**按照顺序**发送这些事件。

* 组合的过程是**分别从**两组`Observable`**各取出一个事件**来进行组合，并且一个事件**只能被使用一次**，组合的顺序是严格按照**事件发送的顺序来进行的**，也就是说不会出现A组事件1和B组事件2进行合并。
* 最终`Observer`收到的事件数量是**发送事件最少数量**。尽管另一组`Observable`还有事件，但是已经没有足够的事件来组合了，因此`Observer`就不会收到剩余的事件了。

```java
 private void initZip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(MainActivity.TAG, "emit 1");
                e.onNext(2);
                Log.d(MainActivity.TAG, "emit 2");
                e.onNext(4);
                Log.d(MainActivity.TAG, "emit 3");
                e.onNext(6);
                Log.d(MainActivity.TAG, "on complete1");
                e.onComplete();
            }
        });

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(MainActivity.TAG, "emit A");
                e.onNext("A");
                Log.d(MainActivity.TAG, "emit B");
                e.onNext("B");
                Log.d(MainActivity.TAG, "emit C");
                e.onNext("C");
                Log.d(MainActivity.TAG, "on complete2");
                e.onComplete();
            }
        });
// param1: observable1 发射事件类型 param2： observable2 发射事件的类型 param3： 合并后的类型
        Observable.zip(observable1, observable, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return "合并后为：" + integer + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(MainActivity.TAG, s);
                Toast.makeText(OperatorActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        });

    }
```
运行结果

```java
01-26 10:00:32.981 16431-16431/D/GEMMILY: emit 1
01-26 10:00:32.981 16431-16431/D/GEMMILY: emit 2
01-26 10:00:32.981 16431-16431/D/GEMMILY: emit 3
01-26 10:00:32.981 16431-16431/D/GEMMILY: on complete1
01-26 10:00:32.981 16431-16431/D/GEMMILY: emit A
01-26 10:00:32.981 16431-16431/D/GEMMILY: 合并后为：2A
01-26 10:00:32.991 16431-16431/D/GEMMILY: emit B
01-26 10:00:32.991 16431-16431/D/GEMMILY: 合并后为：4B
01-26 10:00:33.001 16431-16431/D/GEMMILY: emit C
01-26 10:00:33.001 16431-16431/D/GEMMILY: 合并后为：6C
01-26 10:00:33.001 16431-16431/D/GEMMILY: on complete2
```
此时顺序是先发射`Observable1`后，等`Observable2`发射后再合并。原因是`Observable1`和`Observable2`再同一个线程，要遵守同一线程的执行顺序。
使用`subscribeOn（）`来切换线程可达到二者同时发射的目的。

优化后的运行结果

```
01-26 10:11:14.781 19495-19568/D/GEMMILY: emit A
01-26 10:11:14.781 19495-19567/D/GEMMILY: emit 1
01-26 10:11:14.781 19495-19567/D/GEMMILY: emit 2
01-26 10:11:14.781 19495-19567/D/GEMMILY: emit 3
01-26 10:11:14.781 19495-19567/D/GEMMILY: emit 4
01-26 10:11:14.781 19495-19567/ D/GEMMILY: on complete1
01-26 10:11:14.781 19495-19495/D/GEMMILY: 合并后为：2A
01-26 10:11:14.781 19495-19568/D/GEMMILY: emit B
01-26 10:11:14.781 19495-19568/ D/GEMMILY: emit C
01-26 10:11:14.781 19495-19568/ D/GEMMILY: on complete2
01-26 10:11:14.791 19495-19495/D/GEMMILY: 合并后为：4B
01-26 10:11:14.801 19495-19495/D/GEMMILY: 合并后为：6C
```
>只发射与发射数据项**最少**的那个`Observable`一样多的数据。

应用场景
比如一个界面需要展示的信息, 是要从两个服务器接口中获取, 且都获取到了之后才可展示, 这个时候就可以用`Zip`。

#### Backpressure
##### 背压的引入
使用场景
如果`Observable`发送事件特别快且无限循环发送事件，而`Observer`只能按顺序处理事件，那么待处理的发送事件储存在哪，且储存容量是多少？

```java
// 发送事件在子线程， 处理事件在主线程
Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {

        for (int i = 0; ; i++) {
            e.onNext(i);
        }
    }
}).subscribeOn(Schedulers.io());

observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer s) throws Exception {
                Log.d(MainActivity.TAG, s + "");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(MainActivity.TAG, throwable.toString());
            }
        });

```
查看内存分配结果

![usual](/Users/ksudi/Downloads/usual.png)

当`Observable`和`Observer`同一线程

```java
    Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
//                    Log.d(MainActivity.TAG, "emitter" + i);
                    e.onNext(i);
                }

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "" + integer);
            }
        });
```
查看内存结果

![up](/Users/ksudi/Downloads/up.png)

当发送和接收同一个线程时， 当调用`emitter.onNext(i)`，就相当于直接调用了`Consumer`中的`public void accept(Integer integer) throws Exception {}`。

* 同步订阅关系 `Observable`每发送一个事件必须等到`Observer`接收处理完了以后才能接着发送下一个事件。
* 异步订阅关系 `Observable`发送数据不需要等待`Observer`接收。

**Backpressure**：
因为只有**发送、接收事件**运行在**不同的**线程中，且发送事件的速度**大于**接收事件的速度时，才会产生背压问题。
>事件产生的速度远远快于事件消费的速度，最终导致数据积累越来越多，从而导致OOM等异常。

##### Flowable
在解决背压的时候使用`Flowable`和`Subscribe`，
`Flowable`发送事件，`Subscriber`接收事件，仍通过`subscribe()`连接。

> `Flowable`发射的数据流，以及对数据加工处理的各操作符都添加了背压支持，所以附加了额外的逻辑，其运行效率要比`Observable`低得多。

**BackpressureStrategy 背压策略**

`BackpressureStrategy`是个枚举

* **ERROR** 当`Flowable`的异步缓存池（128）数据超限会抛出异常`MissingBackpressureException`。
* **DROP** 当Flowable的异步缓存池存满时，会丢弃其余数据。
* **LATEST** 当Flowable的异步缓存池存满时，会丢弃其余数据且将**最后一条数据**强行放入缓存池。
* **BUFFER** `Flowable`的缓存池没有固定大小与`Observable`相同，但会导致OOM。
* **MISSING** 创建`Flowable`没指定背压策略， 不会处理数据需要，`Subscriber`通过**背压操作符**指定背压策略。。

**背压操作符**

`Flowable`除了通过create创建的时候指定背压策略，也可以在通过其它创建操作符创建后通过背压操作符指定背压策略。

* **onBackpressureBuffer()** `BackpressureStrategy.BUFFER`
* **onBackpressureDrop()**        `BackpressureStrategy.DROP`
* **onBackpressureLatest()** `BackpressureStrategy.LATEST`

##### Subscription
`Subscription`是`onSubscribe（）` 回调参数，与`Disposable`作用相同，调用`Subscription.cancel()`也可以取消订阅关系，且多了个`request()`方法。

```java
 s.request(Long.MAX_VALUE);   
```

**响应式拉取**

在**RxJava**的观察者模型中，被观察者是主动的推送数据给观察者，观察者是被动接收的。而**响应式拉取**则反过来，观察者主动从被观察者那里去拉取数据，而被观察者变成被动的等待通知再发送数据。 
`Flowable`就是响应式拉取。
1. **error**

```java
  Flowable<Integer> flowable = Flowable.create(
                new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 129; i++) {
                            Log.d(MainActivity.TAG, "emit" + i);
                            e.onNext(i);
                        }
                        Log.d(MainActivity.TAG, "onComplete");
                        e.onComplete();

                    }
                }, BackpressureStrategy.ERROR);

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                // 这里不再是Disposable 而是Subscription
                Log.d(MainActivity.TAG, "onSubscribe");
//                s.request(Long.MAX_VALUE);
                mSubscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(MainActivity.TAG, "onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                Log.w(MainActivity.TAG, "onError: ", t);

            }
        };

flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
```
当超出128时抛出异常

显示结果：
```java
02-01 18:12:41.530 25917-27107/D/GEMMILY: emit128
02-01 18:12:41.530 25917-27107/D/GEMMILY: onComplete
02-01 18:12:41.532 25917-25917/W/GEMMILY: onError:                                                               io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
```
2.**drop**

```java
// 超出存储的直接抛弃
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

                for (int i = 0; ; i++) {
//                    Log.d(MainActivity.TAG, "emit" + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(MainActivity.TAG, "onSubscription");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(MainActivity.TAG, "onNext:" + integer);

                    }
                });
```
超出直接抛弃，存储Subscription，每次执行128个
```java
mSubscription.request(n);
```
结果显示：
```java
02-01 18:27:12.473 25917-25917/D/GEMMILY: onNext:126
02-01 18:27:12.473 25917-25917/D/GEMMILY: onNext:127
02-01 18:28:40.054 25917-25917/V/SettingsInterface:  from settings cache , name = sound_effects_enabled , value = 0
02-01 18:28:40.054 25917-25917/D/GEMMILY: onNext:2025163
02-01 18:28:40.055 25917-25917/D/GEMMILY: onNext:2025164
```
3.**latest**

```java
Flowable.create(new FlowableOnSubscribe<Integer>() {
    @Override
    public void subscribe(FlowableEmitter<Integer> e) throws Exception {

        for (int i = 0; i < 1000; i++) {
            Log.d(MainActivity.TAG, "emit" + i);
            e.onNext(i);
        }
    }
}, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(MainActivity.TAG, "onSubscription");
                mSubscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(MainActivity.TAG, "onNext:" + integer);

            }
```
保留最后一个
结果显示：
```java
02-01 18:33:40.330 25917-25917/D/GEMMILY: onNext:126
02-01 18:33:40.330 25917-25917/D/GEMMILY: onNext:127
02-01 18:33:45.154 25917-25917/V/SettingsInterface:  from settings cache , name = sound_effects_enabled , value = 0
02-01 18:33:45.155 25917-25917/D/GEMMILY: onNext:999
```

                                                        


