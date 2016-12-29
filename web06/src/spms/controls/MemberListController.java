package spms.controls;

import java.util.Map;

import spms.dao.MemberDao;

public class MemberListController implements Controller {
	private MemberDao memberDao;
  //DI 주입을 위한 역할
	public MemberListController setMemberDao(MemberDao memberDao){
		this.memberDao = memberDao;
		return this;
	}
	@Override
	public String execute(Map<String, Object> model) throws Exception {
	    // 회원 목록 데이터를 Map 객체에 저장한다.
	    model.put("members", memberDao.selectList());	    
	    // 화면을 출력할 페이지의 URL을 반환한다.
	    return "/member/MemberList.jsp";
  }
}
