package function

import (
	"context"
	"errors"
	"fmt"
	"os"

	cloudevents "github.com/cloudevents/sdk-go/v2"
)

var (
	token                = os.Getenv("TELEGRAM_API_KEY")
	getFileInfoUrl       = "https://api.telegram.org/bot" + token + "/getFile"
)

type Message struct {
	Chat   map[string]interface{}   `json:"chat"`
	Text   string                   `json:"text"`
	//Date   int64                     `json:"date"`
  From   map[string]interface{}   `json:"from"`
}

type Response struct {
	Chat string `json:"chat"`
	Text string `json:"text"`
	//Date int64 `json:"date"`
	Username string `json:"username"`
	IsBot bool `json:"isbot"`
}

type GetUrlResult struct {
	OK     string                 `json:"ok"`
	Result map[string]interface{} `json:"result"`
}

// Handle a CloudEvent.
func Handle(ctx context.Context, event cloudevents.Event) (resp *cloudevents.Event, err error) {

  fmt.Println("In handle method")

	if token == "" {
		// With no API token we can't do anything
		return nil, errors.New("TELEGRAM_API_KEY environment variable not set")
	}

  fmt.Println("At 1")

	if err = event.Validate(); err != nil {
		fmt.Fprintf(os.Stderr, "invalid event received. %v", err)
		return
	}

  fmt.Println("At 2")

	msg := &Message{}
	event.DataAs(msg)
	if err = event.DataAs(msg); err != nil {
		fmt.Fprintf(os.Stderr, "failed to parse Telegram message %s\n", err)
		return
	}

  fmt.Println("At 3")

	// Extract the data from the Telegram message response
	var chatID string
	if id, found := msg.Chat["id"]; found {
		chatID = id.(string)
	} else {
		fmt.Fprintf(os.Stderr, "No Chat ID in received message.\n")
		return
	}

  fmt.Println("At 4")

	// Message Text
	var msgText string

	msgText = msg.Text
	if msgText == "" {
		fmt.Fprintf(os.Stderr, "No message text in recieved message.\n")
		return
	}

	// Message Date
	//var msgDate int64

	//msgDate = msg.Date

	fmt.Println("At 5")

	// Username
	var msgUsername string
	if inputUsername, found := msg.From["last_name"]; found {
		msgUsername = inputUsername.(string)
	} else {
		fmt.Fprintf(os.Stderr, "No username in from component of received message.\n")
		return
	}

  fmt.Println("At 6")

	// IsBot
	var msgIsbot bool
	if inputIsbot, found := msg.From["is_bot"]; found {
		msgIsbot = inputIsbot.(bool)
	} else {
		fmt.Fprintf(os.Stderr, "No is_bot label in received message.\n")
		return
	}

  fmt.Println("At 7")

	fmt.Println( "  Collated data.\n")
	fmt.Printf( "  ChatID: %s\n",chatID)
	fmt.Printf( "  Text: %s\n", msgText)
	fmt.Printf( "  User: %s\n", msgUsername)
  fmt.Printf( "  IsBot: %t\n", msgIsbot )

	response := cloudevents.NewEvent()
	response.SetID(event.ID())
	response.SetSource("function:receiver")
	response.SetType("telegram.message.processed")
	response.SetData(cloudevents.ApplicationJSON, Response{
		Chat: chatID,
		Text: msgText,
//		Date: msgDate,
		Username: msgUsername,
		IsBot: msgIsbot,
	})
	
  resp = &response

	if err = resp.Validate(); err != nil {
		fmt.Printf("invalid event created. %v", err)
	}

	return
}