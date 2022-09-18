### 전자액자

- 사진 저장소 접근 권한 요청

  - ContextCompat의 checkSelfPermission 메서드를 활용해서 권환 확인
  - PackageManager로 반환됨
    - PERMISSION_GRANTED : 접근 가능
    - PERMISSION_DENIED : 접근 제한

- 범위지정함수를 사용하여 ImageViewList 초기화

  > **범위지정함수 사용규칙**
  >
  > - apply
  >
  >   - 수신 객체 람다 내부에서 수신 객체의 함수나 프로퍼티를 사용하지 않고 수신 객체 자신을 다시 반환하는 경우
  >
  >   - 주로 객체 초기화에 사용
  >
  >     ```kotlin
  >     val peter = Person().apply {
  >       name = "Peter"
  >       age = 18
  >     }
  >     // apply를 사용하지 않고 위와 동일한 코드
  >     val clark = Person()
  >     clark.name = "Clark"
  >     clark.age = 18
  >     ```
  >
  > - also
  >
  >   - 수신 객체 람다가 전달된 수신 객체를 사용하지 않거나 수신 객체의 속성을 변경하지 않고 사용하는 경우
  >
  >   - apply와 마찬가지로 수신 객체를 반환하므로 블록함수가 다른 값을 반환하는 경우는 사용할 수 없다
  >
  >   - 객체의 프로퍼티에 데이터를 할당하기 전에 데이터 유효성 검사할 때 유용하다
  >
  >     ```kotlin
  >     class Book(author: Person) {
  >       val author = author.also { // 수신 객체 자신을 반환
  >         requireNotNull(it.age) // 데이터 할당 전 유효성 검사
  >         print(it.name)
  >       }
  >     }
  >     // also를 사용하지 않고 위와 동일한 코드
  >     class Book(val author: Person) {
  >       // 객체 생성 시 init 함수 활용
  >       init {
  >         requireNotNull(author.age)
  >         print(author.name)
  >       }
  >     }
  >     ```
  >
  > - let
  >
  >   - 람다 블럭의 수행 결과를 반환한다.
  >
  >   - collection 함수의 결과물을 let으로 하나 이상의 작업을 수행하는 경우
  >
  >     ```kotlin
  >     val numbers = mutableListOf("one", "two", "three", "four", "five")
  >     numbers.map { it.length }.filter { it > 3 }.let { 
  >         println(it)
  >         // and more function calls if needed
  >     } // 출력결과 : [5, 4, 4]
  >     ```
  >
  >   - 지정된 값이 null이 아닐 때 코드를 실행해야 하는 경우 : `?.let {...}`
  >
  >     ```kotlin
  >     val str: String? = "HI"
  >     //processNonNullString(str)       // compilation error: str can be null
  >     val length = str?.let { 
  >         println("let() called on $it")        
  >         processNonNullString(it)      // OK: 'it' is not null inside '?.let { }'
  >         it.length
  >     }
  >     ```
  >
  >     processNonNullString이 Nullable한 파라미터를 허용하지 않는 경우 컴파일 에러 발생
  >
  >   - Nullable 객체를 다른 Nullable 객체로 반환하는 경우
  >
  >     ```kotlin
  >     val driversLicence: Licence? = getNullablePerson()?.let {
  >         // nullable personal객체를 nullable driversLicence 객체로 변경합니다.
  >         licenceService.getDriversLicence(it) 
  >     }
  >     // let 을 사용하지 않는 경우
  >     val driver: Person? = getDriver()
  >     val driversLicence: Licence? = if (driver == null) null else
  >         licenceService.getDriversLicence(it)
  >     ```
  >
  >   - 단일지역변수의 범위를 제한하는 경우
  >
  >     ```kotlin
  >     val person: Person = getPerson()
  >     getPersonDao().let { dao -> 
  >         // 변수 dao 의 범위를 이 블록 안으로 제한
  >         dao.insert(person)
  >     }
  >     // let을 사용하지 않는 경우
  >     val person: Person = getPerson()
  >     val personDao: PersonDao = getPersonDao() // 추가적으로 변수 초기화 필요함
  >     personDao.insert(person)
  >     ```
  >
  > - with
  >
  >   - Non-Nullable 수신 객체이고 람다 표현식 안에서 `this` 리시버를 통해 argument로 지정한 수신 객체를 사용한다.
  >   - 람다 표현식의 계산 결과를 리턴한다
  >   - 람다 표현식의 결과가 필요하지 않는 경우에 사용하는 것을 권장한다.
  >   - 한마디로 표현하자면, `with this object, do the following`
  >
  >   ```kotlin
  >   val person: Person = getPerson()
  >   with(person) {
  >       print(name)
  >       print(age)
  >   }
  >   // with을 사용하지 않는 경우
  >   val person: Person = getPerson()
  >   print(person.name)
  >   print(person.age)
  >   ```
  >
  > - run
  >
  >   - let과 비슷하지만, 수신 객체를 `this`리시버로 사용한다.
  >
  >   - 객체를 초기화하면서 값을 계산하고 리턴해야 되는 경우
  >
  >     ```kotlin
  >     val result = service.run {
  >         port = 8080
  >         query(prepareRequest() + " to port $port")
  >     }
  >     // 출력결과 : Result for query 'Default request to port 8080'
  >     ```
  >
  >   - 매개 변수로 전달된 명시적 수신 객체를 암시적 수신 객체로 변환하는 경우
  >
  >     ```kotlin
  >     fun printAge(person: Person) = person.run {
  >         // person 을 수신객체로 변환하여 age 값을 사용합니다.
  >         print(age)
  >     }
  >     ```
  >
  >     



### 참고자료

### 참고자료

- [코틀린 의 apply, with, let, also, run 은 언제 사용하는가?](https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%9D%98-apply-with-let-also-run-%EC%9D%80-%EC%96%B8%EC%A0%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%EA%B0%80-4a517292df29)

- [Scope Function](https://kotlinlang.org/docs/scope-functions.html)