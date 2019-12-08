public interface GameObject {
	public void render(ScreenRender render, int xZoom, int yZoom);

	public void update(UPGame game);

	public Rectangle getRectangle();

}