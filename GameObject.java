public interface GameObject {
	public void render(Renderer render, int xZoom, int yZoom);

	public void update(Game game);

	public Rectangle getRectangle();

}