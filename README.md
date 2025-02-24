# DESX Encryption Algorithm
This project implements the DESX Encryption Algorithm as a JavaFX desktop application. DESX is an extended version of the Data Encryption Standard (DES), designed to increase security against brute-force and differential cryptanalysis attacks.

## Overview
DESX is an enhancement of DES that introduces key whitening, making it more resistant to cryptographic attacks. It follows the same Feistel network structure as DES but applies additional XOR operations before and after the standard DES encryption.

## Features
Desktop GUI: A user-friendly JavaFX interface for encryption and decryption.

Key Generation: Generates three 64-bit keys (K, K1, and K2) for key whitening.

Encryption: Uses DESX encryption to transform plaintext into ciphertext.

Decryption: Recovers the original plaintext from the ciphertext using the same DESX process in reverse.
