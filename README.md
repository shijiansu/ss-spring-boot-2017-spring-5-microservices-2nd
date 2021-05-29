# 2017.spring-5.0-microservices-2nd

![](https://img.shields.io/badge/language-xxx-blue)
![](https://img.shields.io/badge/technology-xxx,%20xxx-blue)
![](https://img.shields.io/badge/development%20year-2021-orange)
![](https://img.shields.io/badge/contributor-shijian%20su-purple)

![](https://img.shields.io/github/languages/top/shijiansu/2017.spring-5.0-microservices-2nd)
![](https://img.shields.io/github/languages/count/shijiansu/2017.spring-5.0-microservices-2nd)
![](https://img.shields.io/github/languages/code-size/shijiansu/2017.spring-5.0-microservices-2nd)
![](https://img.shields.io/github/repo-size/shijiansu/2017.spring-5.0-microservices-2nd)
![](https://img.shields.io/github/last-commit/shijiansu/2017.spring-5.0-microservices-2nd?color=red)

## Outline

- Demystifying Microservices
  - Principles of microservices
    - Single responsibility per service
    - Microservices are autonomous
  - Characteristics of microservices
    - Services are first class citizens
    - Microservices are lightweight
    - Microservices with polyglot architecture
    - Automation in microservices environment
    - Microservices with a supporting ecosystem
    - Microservices are distributed and dynamic
    - Antifragility, fail fast, and self healing
  - Microservices benefits
    - Supports polyglot architecture
    - Enables experimentation and innovation
    - Elastically and selectively scalable
    - Allows substitution
    - Enables to build organic systems
    - Helps managing technology debt
    - Allowing co-existence of different versions
    - Supporting building self-organizing systems
    - Supporting event-driven architecture
    - Enables DevOps
- Related Architecture Styles and Use Cases
  - Service-Oriented Architecture (SOA)
  - Twelve-Factor Apps
    - Single code base
    - Bundle dependencies
    - Externalizing configurations
    - Backing services are addressable
    - Isolation between build, release, and run
    - Stateless, shared nothing processes
    - Expose services through port bindings
    - Concurrency for scale out
    - Disposability, with minimal overhead
    - Development, production parity
    - Externalizing logs
    - Package admin processes
  - Serverless computing (Serverless computing architecture or Functions as a Service (FaaS))
  - Lambda architecture
  - DevOps, Cloud, and Containers
    - DevOps as the practice and process for microservices
    - Cloud and Containers as the self-service infrastructure for microservices
  - Reactive microservices
- Applying Microservices Concepts
  - Microservice design guidelines
    - Deciding microservice boundaries
      - Autonomous functions
      - Size of the deployable unit
      - Most appropriate function or sub-domain
      - Polyglot architecture
      - Selective scaling
      - Agile teams and co-development
      - Single responsibility
      - Replicability or changeability
      - Coupling and cohesion
      - Think of the microservice as a product
    - Designing communication styles
      - Synchronous style communication
      - Asynchronous style communication
    - Orchestration of microservices
    - How many endpoints - one or many?
    - How many microservices per VM - one or multiple?
    - Rules engine - shared or embedded?
    - Role of BPM and workflows
    - Can microservices share a data store?
    - Can microservices be headless?
    - Deciding transaction boundaries
      - Altering use cases to simplify transactional requirements
      - Distributed transaction scenarios
    - Service endpoint design consideration
      - Contract design
      - Protocol selection
        - Message oriented services
        - HTTP and REST endpoints
        - Optimized Communication Protocols
        - API documentations
    - Handling shared libraries
    - User interfaces in microservices
    - Use of API gateways in microservices
    - Use of ESB and iPaaS with microservices
    - Service versioning considerations
    - Design for cross origin
    - Handling shared reference data
    - Microservices and bulk operations
- Microservices Capability Model
  - Microservices capability model
  - Core capabilities
    - Service listeners and libraries
    - Storage capability
    - Service implementation
    - Service endpoint
  - Infrastructure capabilities
    - Cloud
    - Container runtime
    - Container orchestration
  - Supporting capabilities
    - Service gateway
    - Software-defined load balancer
    - Central log management
    - Service discovery
    - Security service
    - Service configuration
    - Ops monitoring
    - Dependency management
    - Data lake
    - Reliable messaging
  - Process and governance capabilities
    - DevOps
    - Automation tools
    - Container registry
    - Microservice documentation
    - Reference architecture and libraries
  - Microservices maturity model
    - Level 0 - Traditional
    - Level 1 - Basic
    - Level 2 - Intermediate
    - Level 3 - Advanced
  - Entry points for adoption
- Microservices Evolution – A Case Study
  - Understanding the PSS application
    - Business process view
    - Functional view
    - Architecture view
    - Design view
    - Implementation view
    - Deployment view
  - Death of the monolith
    - Pain points
    - Stopgap fix
    - Retrospection
      - Precedence on data sharing over modularity
      - Single monolithic database
        - Native queries
        - Stored procedures
        - Compromised on domain boundaries
  - Microservices to the rescue - a planned approach for migration
    - The business case
    - Migration approach
    - Identification of microservices' boundaries
    - Analyze dependencies
      - Events as opposed to query
      - Events as opposed to synchronous updates
      - Challenge requirements
      - Challenge service boundaries
      - Final dependency graph
    - Prioritizing microservices for migration
    - Data synchronization during migration
    - Managing reference data
    - User interfaces and web applications
      - Session handling and security
    - Test strategy
    - Building ecosystem capabilities
    - Migrate modules only if required
    - Internal layering of microservices
    - Orchestrating microservices
    - Integration with other systems
    - Managing shared libraries
    - Handling exceptions
  - Target implementation
    - Implementation projects
    - Running and testing the project
  - Potential next steps
- Scale Microservices with Spring Cloud Components
  - What is Spring Cloud?
  - Spring Cloud releases
  - Setting up the environment for the BrownField PSS
  - Spring Cloud Config
    - Building microservices with Config Server
    - Setting up the Config Server
    - Understanding the Config Server URL
      - Accessing the Config Server from clients
    - Handling configuration changes
    - Spring Cloud Bus for propagating configuration changes
    - Setting up high availability for the Config Server
    - Monitoring Config Server health
    - Config Server for configuration files
    - Completing changes to use Config Server
  - Eureka for registration and discovery
    - Understanding dynamic service registration and discovery
    - Understanding Eureka
    - Setting up the Eureka Server
    - High availability for Eureka
  - Zuul proxy as the API Gateway
    - Setting up Zuul
    - High availability of Zuul
      - High availability of Zuul when the client is also a Eureka Client
      - High availability when client is not a Eureka Client
      - Completing Zuul for all other services
  - Streams for reactive microservices
  - Protecting microservices with Spring Cloud Security
  - Summarising the BrownField PSS architecture
- Logging and Monitoring Microservices
  - Understanding log management challenges
  - Centralized logging solution
  - Selection of logging solutions
    - Cloud services
    - Off-the-shelf solutions
    - Best of the breed integration
      - Log shippers
      - Log stream processors
      - Log storage
      - Dashboards
    - Custom logging implementation
    - Distributed tracing with Spring Cloud Sleuth
  - Monitoring microservices
    - Monitoring challenges
    - Monitoring tools
    - Monitoring microservice dependency
    - Spring Cloud Hystrix for fault-tolerant microservices
    - Aggregate Hystrix streams with Turbine
  - Data analysis using Data Lake
- Containerizing Microservices with Docker
  - Understanding gaps in the BrownField PSS microservices
  - Difference between VM and containers
  - Benefits of containers
  - Microservices and containers
  - Introduction to Docker
    - Key components of Docker
      - The Docker daemon
      - The Docker client
      - The Docker image
      - The Docker container
      - The Docker registry
      - Dockerfile
  - Deploying microservices into Docker
  - Running RabbitMQ on Docker
  - Using the Docker registry
    - Setting up the Docker Hub
    - Publish microservices to the Docker Hub
  - Microservices on Cloud
    - Installing Docker on AWS EC2
  - Running BrownField services on EC2
  - Future of containerization
- Scaling Dockerized Microservices with Mesos and Marathon
  - Scaling microservices
    - Understanding autoscaling
    - The missing pieces
  - Container orchestration
    - Why is container orchestration is important
    - What does container orchestration do?
    - Relationship with microservices
    - Relationship with virtualization
    - Container orchestration solutions
      - Docker Swarm
      - Kubernetes
      - Apache Mesos
      - HashiCorp Nomad
      - CoreOS Fleet
  - Container orchestration with Mesos and Marathon
    - Mesos in details
      - Mesos architecture
      - Marathon
  - Implementing Mesos and Marathon with DCOS
  - Implementing Mesos and Marathon for BrownField microservices
    - Installing Mesos, Marathon, and related components
    - Running Mesos and Marathon
  - Preparing BrownField PSS services
    - Deploying BrownField PSS services
- Microservice Development Life Cycle
  - Practice points for microservice development
    - Understanding business motivation and value
    - Change the mindset from project to product development
    - Choosing the right development philosophy
    - Using the concept of minimum viable product (MVP)
    - Overcoming the legacy hotspot
    - Establishing self-organizing teams
    - Building the self-service cloud
    - Building a microservices ecosystem
    - DevOps as a life cycle process
    - Value driven planning
    - Continuous monitoring and feedback
  - Automating development cycle
    - Development
    - Integration
    - Testing
      - Sanity tests
      - Regression testing
      - Functional testing
      - Acceptance testing
      - Performance testing
      - Real user flow simulation or Journey testing
      - Security testing
      - Exploratory testing
      - A/B testing, Canary testing, and blue-green deployments
      - Other non-functional tests
      - Testing in production (TiP)
      - Antifragility testing
    - Deployment
    - Monitoring and feedback
    - Configuration management
    - Microservices development governance, reference architectures, and libraries

## Source code

- https://github.com/PacktPublishing/Spring-5.0-Microservices-Second-Edition

## Microservice migration approach

a number of key questions need to be answered from the transition point of view. These are listed as follows:

- Identification of microservices’ boundaries
- Prioritizing microservices for migration
- Handling data synchronization during the transition phase
- Handling user interface integration, working with old and new user interfaces
- Handling of reference data in the new system
- Testing strategy to ensure the business capabilities are intact and correctly reproduced
- Identification of any prerequisites for microservice development, such as microservices capabilities, frameworks, processes, and more

## Docker Swarm

Docker Swarm works with the concept of manager and nodes. The manager is the single point for administrations to interact and schedule the Docker containers for execution. The nodes are where the Docker containers are deployed and run.

## Kubernetes

Kubernetes architecture has the concept of master, nodes, and pods. The master and nodes together are called a Kubernetes cluster. The master node is responsible for allocating and managing the workload across a number of nodes. Nodes are nothing but a VM or a physical machine. Nodes are further subsegmented as pods. A node can host multiple pods. One or more containers are grouped and executed inside a pod.

## Execute all tests in repo

`/bin/bash run-repo-test.sh`
