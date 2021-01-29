package functions;

public class Input 
{
  private String _chat = null;
  private String _text = null;
  private String _username = null;
  private boolean _isBot = null;

  public Input() {}

  public Input( String chat, String text, String username, boolean isBot ) 
  {
    _chat = chat;
    _text = text;
    _username = username;
    _isBot = isBot;
  }

  // Accessors
  public String getChat() { return _chat;}
  public String getText() { return _text; }
  public String getUsername() { return _username; }
  public boolean getIsBot() { return _isBot; }

  // Mutators
  public void setChat( String chat ) { _chat = chat; }
  public void setText( String text ) { _text = text; }
  public void setUsername( String username ) { _username = username; }
  public void setIsBot( boolean isBot ) { _isBot = isBot; }
}
