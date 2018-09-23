# Product Deliveries

This repository contains classes for retrieving list of data related to product which need to be deliver to at some place from the REST Api.
It uses MVP design with RX Android, Retrofit 2, Picasso, ButterKnife, libraries. 
This Product Deliveries application gives list of data with their title, images and content with map on click.

## Dependency

This app is developed with Android Studio 3.1.1 and latest Gradle version 3.1.1 Hence use -Android Studio 3.0 or Above version 
to edit or for testing at your own machine.
Compile SDK version 27 and Min SDK 18.

## Features

It shows product articles with their title, Description and Image in a list.
User is able to scroll and refresh as per need via swipe down on the screen.
It uses RxAndroid with MVP in its architecture.
It uses Retrofit 2 library for network calls.
Its uses Picasso for image download and cache management.
ButterKnife is used with views

## Google Map Key Generation Process

https://console.developers.google.com/apis/dashboard
First need to go to developer console with the help of above webiste link.
1) Create a new project
2) Enable Map SDK for Android
3) Go to Create Credentials and choose API Key
4) Put that api key in string param "google_maps_key" in strings.xml 

## Folder Structure

This application's structure can be divided into 2 parts Business and UI.
Business layer contains - Data = contains core API and Usecase classes for retrofit 2 calls - model = contain the POJO classes DataObject 
and DomainModel as JSON responses
UI layer contains - Activity, MVP(presenter, contract interface and Recylcer view adapter)

## BUILD and RUN

Use Android Studio 3.1.1 and Gradle 3.1.1 to build the project.
