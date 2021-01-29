package functions;

public class MessageOutput
{
  private String text = null;
  private String username = null;
  private String chatid = null;
  private boolean isbot = false;

  // Setters
  public void setText( String textInput )
  {
    this.text = textInput;
  }

  public void setUsername( String usernameInput )
  {
    this.username = usernameInput;
  }

  public void setChatid( String chatidInput )
  {
    this.chatid = chatidInput;
  }

  public void setIsbot( boolean isbotInput )
  {
    this.isbot = isbotInput;
  }

  public void setDate( String dateInput )
  {
    this.date = dateInput;
  }

  // Accessors
  public String getText() { return this.text; }
  public String getUsername() { return this.username; }
  public String getChatid() { return this.chatid; }
  public boolean getIsbot() { return this.isbot; }
}