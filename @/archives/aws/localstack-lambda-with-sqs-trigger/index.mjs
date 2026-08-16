export const handler = async (event) => {
	const messages = event.Records.map(record => record.body);
	messages.forEach(message => console.log(message));
	return messages;
};
