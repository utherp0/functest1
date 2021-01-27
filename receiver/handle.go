package function

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"strings"

	cloudevents "github.com/cloudevents/sdk-go/v2"
)

var (
	token                = os.Getenv("TELEGRAM_API_KEY")
	getFileInfoUrl       = "https://api.telegram.org/bot" + token + "/getFile"
	donwloadPhotoBaseUrl = "https://api.telegram.org/file/bot" + token + "/"
)

type Message struct {
	Chat   map[string]interface{}   `json:"chat"`
	Text   string                   `json:"text"`
	Date   string                   `json:"date"`
  From   map[string]interface{}   `json:"from"`
}

type Response struct {
	Chat string `json:"chat"`
	Text string `json:"text"`
	Date string `json:"text"`
	Username string `json:"username"`
	IsBot bool `json:"isbot"`
}

type GetUrlResult struct {
	OK     string                 `json:"ok"`
	Result map[string]interface{} `json:"result"`
}

// Handle a CloudEvent.
func Handle(ctx context.Context, event cloudevents.Event) (resp *cloudevents.Event, err error) {
	if token == "" {
		// With no API token we can't do anything
		return nil, errors.New("TELEGRAM_API_KEY environment variable not set")
	}

	if err = event.Validate(); err != nil {
		fmt.Fprintf(os.Stderr, "invalid event received. %v", err)
		return
	}

	msg := &Message{}
	event.DataAs(msg)
	if err = event.DataAs(msg); err != nil {
		fmt.Fprintf(os.Stderr, "failed to parse Telegram message %s\n", err)
		return
	}

	// Extract the data from the Telegram message response
	var chatID string
	if id, found := msg.Chat["id"]; found {
		chatID = id.(string)
	} else {
		fmt.Fprintf(os.Stderr, "No Chat ID in received message.\n")
		return
	}

	// Message Text
	var msgText string
	if inputText, found := msg.Text; found {
		msgText = inputText
	} else {
		fmt.Fprintf(os.Stderr, "No message text in recieved message.\n")
		return
	}

	// Message Date
	var msgDate string
	if inputDate, found := msg.Date; found {
		msgDate = inputDate
	} else {
		fmt.Fprintf(os.Stderr, "No date in received message.\n")
		return
	}

	// Username
	var msgUsername string
	if inputUsername, found := msg.From["username"]; found {
		msgUsername = inputUsername.(string)
	} else {
		fmt.Fprintf(os.Stderr, "No username in from component of received message.\n")
		return
	}

	// IsBot
	var msgIsbot bool
	if inputIsbot, found := msg.From["is_bot"]; found {
		msgIsbot = inputIsbot.(bool)
	} else {
		fmt.Fprintf(os.Stderr, "No is_bot label in received message.\n")
		return
	}

	response := cloudevents.NewEvent()
	response.SetID(event.ID())
	response.SetSource("function:receiver")
	response.SetType("telegram.message.processed")
	response.SetData(cloudevents.ApplicationJSON, Response{
		Chat: chatID,
		Text: msgText,
		Date: msgDate,
		Username: msgUsername,
		IsBot: msgIsbot
	})
	
  resp = &response

	if err = resp.Validate(); err != nil {
		fmt.Printf("invalid event created. %v", err)
	}

	return
}