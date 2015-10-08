package aa;

public class k implements bInter{
	private k() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void gan() {
		System.out.println("k");
		
	}

	@Override
	public bInter getInstance() {
		return new k();
	}

}
