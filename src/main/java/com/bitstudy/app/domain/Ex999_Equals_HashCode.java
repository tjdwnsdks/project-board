package com.bitstudy.app.domain;

public class Ex999_Equals_HashCode {
    public static void main(String[] args) {
        /** 주의사항: 변수 다 같은 값 가지고 비교하기 */

//설명: equals()는 최상위 클래스인 Object에 포함되어 있기 때문에 모든 하위 클래스에서 재정의 해서 사용이 가능하다.
//    2개의 객체가 동일한지 검사하기 위해 사용된다. eqauls가 구현된 방법은 2개의 객체가 참조하는 것이 동일한지를 확인하는 것이다.
//    즉, 2개의 객체가 가리키는 곳이 동일한 메모리 주소일 경우에만 동일한 객체가 된다.

        String txt1 = "txt"; /* 걍 평소 하듯이 변수에 문자값 저장한것뿐... */
        String txt2 = new String("txt"); /* String 클래스 사용 */
        String txt3 = new String("txt"); /* String 클래스 사용 */

        System.out.println("txt1: " + txt1);
        System.out.println("txt2: " + txt2);
        System.out.println("txt3: " + txt3);

        System.out.println("--- == ---");
        System.out.println(txt1 == txt2);
        System.out.println(txt2 == txt3);

        System.out.println("--- equals ---");
        System.out.println(txt1.equals(txt2)); // true
        System.out.println(txt2.equals(txt3)); // true

/* equals - 값이 같으면 걍 true (주소값이 어떻고 저쪄고 상관 없음!!)
            둘 다 null 이어도 true 나옴
   hashCode - 객체를 식별하는 Integer 값입니다.
              객체가 갖고 있는 데이터를 어떤 알고리즘에 적용하여 계산된 정수 값을 hashcode라고 할 수 있습니다.

              Hashcode를 사용하는 이유 중에 하나는, 객체를 비교할 때 드는 비용을 낮추기 위해서 입니다.
              자바에서 2개의 객체가 같은지 비교할 때 equals()를 사용하는데,
              여러 객체를 비교할 때 equals()를 사용하면 Integer를 비교하는 것에 비해 많은 시간이 소요됩니다
*/

/** == 와 equals 차이
 == : "동일성 비교"는 == 비교다. 객체 인스턴스의 "주소 값"을 비교한다.
 ("주소값이 같은지" 아닌지 비교하는 것이고,)


 equals() : "동등성 비교"는 equals() 메소드를 사용해서 객체 "내부의 값"을 비교한다.
 연산도 내부적으로 주소값을 비교하지만 String클래스에서는 equals()를 재정의해 "내용을 비교"하게 되어있다
 * */
    }
}
