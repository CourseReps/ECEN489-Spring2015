# chmbrlnd.github.io


## Installing [Jekyll](http://jekyllrb.com/)

```
sudo gem install jekyll
sudo gem install bundler
xcode-select --install     # Mac OS X
cd /path/to/directory      # Dir with Gemfile
bundle install
```

Note that ```bundle``` should NOT be run as sudo.


## Building or Running [Jekyll](http://jekyllrb.com/) with [Bundler](http://bundler.io/)

```
cd /path/to/directory      # Dir with Gemfile
bundle exec jekyll build
bundle exec jekyll serve   # http://127.0.0.1:4000
```

To keep Jekyll up to date, it is important to issue the ```bundle update``` command periodically.
Running executables without ```bundle exec``` may work, however this is unreliable and is often the source of considerable pain.


## Publishing on ECE Server

Edit ```_config.yml``` and set target ```url``` and ```baseurl```.
Build [Jekyll](http://jekyllrb.com/) with [Bundler](http://bundler.io/).
Push on ECE server.

```
scp -r * chmbrlnd@apollo.ece.tamu.edu:.public_html/
```
