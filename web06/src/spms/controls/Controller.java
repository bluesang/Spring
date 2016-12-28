package spms.controls;

import java.util.Map;

public interface Controller {
  String execute(Map<String, Object> model) throws Exception; //String은 view 이름을 담을 용도 , 회원가입시 입력받은 데이터는 Map으로 넘겨받음
}
