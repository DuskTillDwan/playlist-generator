# Playlist Generator

Playlist Generator is a project to generate playlists 
for different music platforms based on a group chat.
The initial load of songs are loaded via a JSON file, which was downloaded
from Facebook. 

## Local Development

This project leverages [direnv](https://direnv.net) to manage local development variables.
If you find yourself having trouble, you can manually add the .env or .envrc files to the run 
configuration of your project. Just copy and paste the absolute path to your .env file. 

An example of the .envrc file can be found below. Replace the bracketed variables with your applicable
values. Add the below CLI to your local development run configuration to leverage the application-local.properties file.

```bash
-Dspring.profiles.active=local
```

```properties
POSTGRES_USER=<user_name>
POSTGRES_PASSWORD=<password>
POSTGRES_DB=<your_database_name>
```

## Usage

```python
tbd
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)